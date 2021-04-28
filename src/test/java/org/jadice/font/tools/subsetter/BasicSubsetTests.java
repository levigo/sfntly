/*
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jadice.font.tools.subsetter;

import org.jadice.font.sfntly.table.truetype.LocaTable;
import org.jadice.font.sfntly.Font;
import org.jadice.font.sfntly.FontFactory;
import org.jadice.font.sfntly.Tag;
import org.jadice.font.sfntly.table.Table;
import org.jadice.font.sfntly.table.core.CMapTable;
import org.jadice.font.sfntly.table.truetype.GlyphTable;
import org.jadice.font.sfntly.testutils.TestFont;
import org.jadice.font.sfntly.testutils.TestFontUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;

/** @author Stuart Gill */
public class BasicSubsetTests extends TestCase {
  private static final boolean DEBUG = false;
  private static final File fontFile = TestFont.TestFontNames.OPENSANS.getFile();

  public void testSubsetGlyphs() throws Exception {
    int glyphCount = 11;
    Font[] srcFontArray = TestFontUtils.loadFont(fontFile);
    Font srcFont = srcFontArray[0];

    LocaTable srcLocaTable = srcFont.getTable(Tag.loca);
    GlyphTable srcGlyphTable = srcFont.getTable(Tag.glyf);

    List<Integer> srcLoca = new ArrayList<>(glyphCount + 1);
    for (int i = 0; i <= glyphCount + 1; i++) {
      srcLoca.add(srcLocaTable.loca(i));
    }

    FontFactory factory = FontFactory.getInstance();
    Subsetter subsetter = new DumbSubsetter(srcFont, factory);
    // BitSet glyphs = new BitSet();
    // glyphs.set(0, 10);
    List<Integer> glyphs = new ArrayList<>(glyphCount);
    glyphs.add(0);
    glyphs.add(1);
    glyphs.add(2);
    glyphs.add(3);
    glyphs.add(4);
    glyphs.add(5);
    glyphs.add(6);
    glyphs.add(7);
    glyphs.add(8);
    glyphs.add(9);
    glyphs.add(11);
    glyphs.add(10);
    subsetter.setGlyphs(glyphs);

    List<CMapTable.CMapId> cmapIds = new ArrayList<>();
    cmapIds.add(
        CMapTable.CMapId.getInstance(
            Font.PlatformId.Macintosh.value(), Font.MacintoshEncodingId.Mongolian.value()));
    cmapIds.add(CMapTable.CMapId.WINDOWS_BMP);
    subsetter.setCMaps(cmapIds, 1);

    Set<Integer> removeTables = new HashSet<>();
    removeTables.add(Tag.GPOS);
    removeTables.add(Tag.GSUB);
    removeTables.add(Tag.kern);
    subsetter.setRemoveTables(removeTables);
    Font.Builder dstFontBuilder = subsetter.subset();

    Map<Integer, Table.Builder<? extends Table>> tableBuilders = dstFontBuilder.tableBuilderMap();
    Set<Integer> builderTags = dstFontBuilder.tableBuilderMap().keySet();
    Font dstFont = dstFontBuilder.build();
    LocaTable dstLocaTable = dstFont.getTable(Tag.loca);

    // TODO(stuartg): subsetter needs to modify other tables with the new glyph
    List<Integer> dstLoca = new ArrayList<>(glyphCount + 1);
    for (int i = 0; i <= glyphCount + 1; i++) {
      dstLoca.add(dstLocaTable.loca(i));
    }

    for (int i = 0; i <= 10; i++) {
      assertEquals(srcLoca.get(i), dstLoca.get(i));
    }
    assertEquals(srcLoca.get(11) - srcLoca.get(10), dstLoca.get(12) - dstLoca.get(11));
    assertEquals(srcLoca.get(12) - srcLoca.get(11), dstLoca.get(11) - dstLoca.get(10));

    CMapTable cmapTable = dstFont.getTable(Tag.cmap);
    assertNotNull(cmapTable.cmap(CMapTable.CMapId.WINDOWS_BMP));
    assertEquals(1, cmapTable.numCMaps());

    // make sure tables removed
    assertFalse(dstFont.hasTable(Tag.GPOS));
    assertFalse(dstFont.hasTable(Tag.GSUB));
    assertFalse(dstFont.hasTable(Tag.kern));

    if (DEBUG) {
      File dstFontFile = TestFontUtils.serializeFont(dstFont, ".ttf");
      System.out.println(dstFontFile);
    }
  }
}
