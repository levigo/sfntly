package org.jadice.font.sfntly.table.opentype.contextsubst;

import org.jadice.font.sfntly.table.opentype.component.OffsetRecordTable;
import org.jadice.font.sfntly.data.ReadableFontData;

public abstract class SubGenericRuleSet<T extends DoubleRecordTable> extends OffsetRecordTable<T> {
  protected SubGenericRuleSet(ReadableFontData data, int base, boolean dataIsCanonical) {
    super(data, base, dataIsCanonical);
  }

  @Override
  public int fieldCount() {
    return 0;
  }

  protected abstract static class Builder<
          T extends SubGenericRuleSet<S>, S extends DoubleRecordTable>
      extends OffsetRecordTable.Builder<T, S> {

    protected Builder(ReadableFontData data, boolean dataIsCanonical) {
      super(data, dataIsCanonical);
    }

    protected Builder() {
      super();
    }

    protected Builder(T table) {
      super(table);
    }

    @Override
    protected void initFields() {}

    @Override
    protected int fieldCount() {
      return 0;
    }
  }
}
