// Copyright 2012 Google Inc. All Rights Reserved.

package org.jadice.font.sfntly.table.opentype;

import org.jadice.font.sfntly.data.ReadableFontData;
import org.jadice.font.sfntly.table.opentype.component.GsubLookupType;

/** @author dougfelt@google.com (Doug Felt) */
abstract class GsubLookupSubTable extends LookupSubTable {

  protected GsubLookupSubTable(ReadableFontData data, boolean dataIsCanonical) {
    super(data, dataIsCanonical);
  }

  @Override
  public abstract Builder<? extends GsubLookupSubTable> builder();

  @Override
  public abstract GsubLookupType lookupType();

  abstract static class Builder<T extends GsubLookupSubTable> extends LookupSubTable.Builder<T> {

    protected Builder(ReadableFontData data, boolean dataIsCanonical) {
      super(data, dataIsCanonical);
    }

    protected Builder(T table) {
      super(table);
    }

    @Override
    public abstract GsubLookupType lookupType();
  }
}
