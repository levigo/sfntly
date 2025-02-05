/*
 * Copyright 2010 Google Inc. All Rights Reserved.
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

package org.jadice.font.sfntly.testutils;

import org.jadice.font.sfntly.Font;
import org.jadice.font.sfntly.FontFactory;
import org.jadice.font.sfntly.Tag;
import org.jadice.font.sfntly.table.FontDataTable;
import org.jadice.font.sfntly.table.Header;
import org.jadice.font.sfntly.table.Table;
import org.jadice.font.sfntly.table.core.CMap;
import org.jadice.font.sfntly.table.core.CMapTable;
import org.jadice.font.sfntly.table.core.FontHeaderTable;
import org.jadice.font.sfntly.table.core.MaximumProfileTable;
import org.jadice.font.sfntly.table.core.NameTable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

/** @author Stuart Gill */
public class TestFontUtils {

  private static final Logger logger = Logger.getLogger(Font.class.getCanonicalName());

  private TestFontUtils() {}

  /**
   * Open a file and read any fonts that it contains. In many cases this will be a single font but
   * it may be multiple fonts.
   *
   * @param file the file holding the font(s)
   * @return the fonts that came from the file
   */
  public static Font[] loadFont(File file) throws IOException {
    return loadFont(file, true);
  }

  /**
   * Open a file and read any fonts that it contains. In many cases this will be a single font but
   * it may be multiple fonts.
   *
   * @param file the file holding the font(s)
   * @return the fonts that came from the file
   */
  public static Font[] loadFont(File file, boolean fingerprint) throws IOException {
    FontFactory fontFactory = FontFactory.getInstance();
    fontFactory.fingerprintFont(fingerprint);
    try (FileInputStream is = new FileInputStream(file)) {
      return fontFactory.loadFonts(is);
    }
  }

  /**
   * Open a file and read any fonts that it contains and check the fonts against supplied expected
   * fingerprints.
   *
   * @param file the file holding the font(s)
   * @param expectedFingerprints array of expected fingerprints for each font in the file
   * @return the fonts that came from the file
   */
  public static Font[] loadFont(File file, byte[]... expectedFingerprints) throws IOException {
    Font[] fonts = loadFont(file, true);
    for (int i = 0; i < Math.min(fonts.length, expectedFingerprints.length); i++) {
      if (expectedFingerprints[i] != fonts[i].digest()) {
        throw new IOException(
            "Did not get the expected fingerprint for font#"
                + i
                + " in "
                + file.getPath()
                + " Has the file changed?");
      }
    }
    return fonts;
  }

  /**
   * Open a file and read any fonts that it contains. In many cases this will be a single font but
   * it may be multiple fonts.
   *
   * @param file the file holding the font(s)
   * @return the fonts that came from the file
   */
  public static Font[] loadFontUsingByteArray(File file) throws IOException {
    return loadFontUsingByteArray(file, true);
  }

  /**
   * Open a file and read any fonts that it contains. In many cases this will be a single font but
   * it may be multiple fonts.
   *
   * @param file the file holding the font(s)
   * @return the fonts that came from the file
   */
  public static Font[] loadFontUsingByteArray(File file, boolean fingerprint) throws IOException {
    byte[] b = TestUtils.readFile(file);
    FontFactory fontFactory = FontFactory.getInstance();
    fontFactory.fingerprintFont(fingerprint);
    return fontFactory.loadFonts(b);
  }

  private static final Comparator<Table> TABLE_COMPARATOR_BY_OFFSET =
      Comparator.comparingInt(o -> o.header().offset());

  public static Font[] buildAndCheckFont(FontFactory fontFactory, InputStream is, int debug)
      throws IOException {

    Font[] fontArray = fontFactory.loadFonts(is);

    for (Font font : fontArray) {

      if (debug >= 1) {
        logger.info(font.toString());
      }

      FontHeaderTable head = font.getTable(Tag.head);
      if (head != null) {
        long magicNumber = head.magicNumber();
        if (0x5F0F3CF5 != magicNumber) {
          throw new RuntimeException("Magic number is incorrect.");
        }
        if (debug >= 2) {
          logger.fine("magic number = " + Long.toHexString(magicNumber));
          MaximumProfileTable maxp = font.getTable(Tag.maxp);
          logger.fine("Number of glyphs = " + maxp.numGlyphs());
          logger.fine("\n------ Tables by File Location");
        }
      } else {
        // should find a bhed table if no head table
        FontDataTable bhed = font.getTable(Tag.intValue("bhed"));
        if (bhed == null) {
          throw new RuntimeException("No head table or bhed");
        }
      }

      Set<Table> tables = new TreeSet<>(TABLE_COMPARATOR_BY_OFFSET);
      tables.addAll(font.tableMap().values());

      for (Table table : tables) {
        if (debug >= 2) {
          System.out.println(table);
        }
        Header header = table.header();
        boolean csValid = table.calculatedChecksum() == header.checksum();
        if (!csValid) {
          logger.severe("\t**** Checksum not valid!");
          logger.severe("\tcalculated = 0x" + Long.toHexString(table.calculatedChecksum()));
          logger.severe("\tread       = 0x" + Long.toHexString(header.checksum()));
          throw new RuntimeException("Checksum for table " + Tag.stringValue(header.tag()));
        }
      }

      if (debug >= 2) {
        logger.fine("\n------ CMap Tables");
        CMapTable cmap = font.getTable(Tag.cmap);
        for (CMap cmt : cmap) {
          logger.fine(cmt.toString());
          for (int c = 0; c < 256; c++) {
            logger.finer(c + " = " + cmt.glyphId(c));
          }
        }

        logger.fine("\n----- Name Tables");
        NameTable name = font.getTable(Tag.name);
        for (NameTable.NameEntry entry : name) {
          logger.finer(entry.toString());
        }
      }
    }
    return fontArray;
  }

  public static File serializeFont(File fontFile) throws Exception {
    Font[] fonts = loadFont(fontFile);
    if (fonts == null || fonts.length != 1) {
      return null;
    }
    String extension = TestUtils.extension(fontFile);

    return serializeFont(fonts[0], extension);
  }

  public static File serializeFont(Font font, String extension) throws Exception {
    File serializedFontFile = File.createTempFile("serializedFont_", extension);
    return serializeFont(font, serializedFontFile);
  }

  public static File serializeFont(Font font, File serializedFontFile) throws Exception {
    serializedFontFile.mkdirs();
    serializedFontFile.createNewFile();
    try (OutputStream os = TestUtils.createOutputStream(serializedFontFile)) {
      FontFactory factory = FontFactory.getInstance();
      factory.serializeFont(font, os);
    }
    return serializedFontFile;
  }

  public static Font.Builder builderForFontFile(File fontFile) throws IOException {
    return builderForFontFile(fontFile, true);
  }

  public static Font.Builder builderForFontFile(File fontFile, boolean fingerprint)
      throws IOException {
    FileInputStream is = new FileInputStream(fontFile);
    FontFactory otfFactory = FontFactory.getInstance();
    otfFactory.fingerprintFont(fingerprint);
    Font.Builder[] builder = otfFactory.loadFontsForBuilding(is);
    return builder[0];
  }
}
