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

package org.jadice.font.sfntly;

import org.jadice.font.sfntly.table.Header;
import org.jadice.font.sfntly.table.Table;
import org.jadice.font.sfntly.testutils.TestFont;
import org.jadice.font.sfntly.testutils.TestFontUtils;
import java.io.File;
import java.util.Map;
import junit.framework.TestCase;

/** @author Stuart Gill */
public class LoadingTests extends TestCase {

  private static final File TEST_FONT_FILE = TestFont.TestFontNames.OPENSANS.getFile();

  public LoadingTests() {}

  public LoadingTests(String name) {
    super(name);
  }

  public void testLoadingComparison() throws Exception {
    Font[] sFonts = TestFontUtils.loadFont(TEST_FONT_FILE);
    Font[] bFonts = TestFontUtils.loadFontUsingByteArray(TEST_FONT_FILE);

    assertEquals(sFonts.length, bFonts.length);
    for (int i = 0; i < sFonts.length; i++) {
      Font streamFont = sFonts[i];
      Font byteFont = bFonts[i];

      assertEquals(streamFont.numTables(), byteFont.numTables());
      Map<Integer, ? extends Table> streamTableMap = streamFont.tableMap();
      for (Map.Entry<Integer, ? extends Table> entry : streamTableMap.entrySet()) {
        int tag = entry.getKey();
        Table streamTable = entry.getValue();
        Table byteTable = byteFont.getTable(tag);

        assertNotNull(byteTable);
        assertEquals(streamTable.dataLength(), byteTable.dataLength());
        assertEquals(streamTable.calculatedChecksum(), byteTable.calculatedChecksum());

        Header streamHeader = streamTable.header();
        Header byteHeader = byteTable.header();

        assertEquals(streamHeader.checksum(), byteHeader.checksum());
        assertEquals(streamHeader.length(), byteHeader.length());
        assertEquals(streamHeader.offset(), byteHeader.offset());
        assertEquals(streamHeader.tag(), byteHeader.tag());
      }
    }
  }
}
