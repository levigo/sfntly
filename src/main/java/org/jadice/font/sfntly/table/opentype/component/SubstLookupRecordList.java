package org.jadice.font.sfntly.table.opentype.component;

import org.jadice.font.sfntly.data.ReadableFontData;
import org.jadice.font.sfntly.data.WritableFontData;

public final class SubstLookupRecordList extends RecordList<SubstLookupRecord> {
  private SubstLookupRecordList(WritableFontData data) {
    super(data);
  }

  public SubstLookupRecordList(ReadableFontData data, int base) {
    super(data, 0, base);
  }

  public SubstLookupRecordList(ReadableFontData data, int countOffset, int valuesOffset) {
    super(data, 0, countOffset, valuesOffset);
  }

  @Override
  protected SubstLookupRecord getRecordAt(ReadableFontData data, int offset) {
    return new SubstLookupRecord(data, offset);
  }

  @Override
  protected int recordSize() {
    return SubstLookupRecord.RECORD_SIZE;
  }
}
