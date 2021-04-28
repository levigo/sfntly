package org.jadice.font.sfntly.issue_tests;

import org.jadice.font.sfntly.testutils.TestFont;
import org.jadice.font.sfntly.Font;
import org.jadice.font.sfntly.FontFactory;
import org.jadice.font.sfntly.Tag;
import org.jadice.font.sfntly.table.core.NameTable;

import java.io.File;
import java.io.FileInputStream;
import junit.framework.TestCase;

/*
 * Test for <a href="https://github.com/googlei18n/sfntly/issues/27">Issue 27</a>.
 * <p>
 * Adding a zero length name that sorts at the end of the name table.
 */
public class Issue27Tests extends TestCase {

  private static final File fontFile = TestFont.TestFontNames.OPENSANS.getFile();

  public void testIssue27() throws Exception {
    FontFactory fontFactory = FontFactory.getInstance();
    Font.Builder fontBuilder = fontFactory.loadFontsForBuilding(new FileInputStream(fontFile))[0];
    NameTable.Builder nameTableBuilder = (NameTable.Builder) fontBuilder.getTableBuilder(Tag.name);
    // add a name that will sort after all the other names in the table
    nameTableBuilder
        .nameBuilder(
            Font.PlatformId.Windows.value(),
            Font.WindowsEncodingId.UnicodeUCS4.value(),
            NameTable.WindowsLanguageId.Spanish_UnitedStates.value(),
            NameTable.NameId.WWSSubfamilyName.value())
        .setName("");
    Font font = fontBuilder.build();
    assertNotNull(font);
  }
}
