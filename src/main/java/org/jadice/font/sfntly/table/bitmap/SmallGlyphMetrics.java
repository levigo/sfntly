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

package org.jadice.font.sfntly.table.bitmap;

import org.jadice.font.sfntly.data.ReadableFontData;
import org.jadice.font.sfntly.data.WritableFontData;

/** @author Stuart Gill */
public class SmallGlyphMetrics extends GlyphMetrics {

  private interface Offset {
    int height = 0;
    int width = 1;
    int BearingX = 2;
    int BearingY = 3;
    int Advance = 4;
    int SIZE = 5;
  }

  private SmallGlyphMetrics(ReadableFontData data) {
    super(data);
  }

  public int height() {
    return data.readByte(Offset.height);
  }

  public int width() {
    return data.readByte(Offset.width);
  }

  public int bearingX() {
    return data.readChar(Offset.BearingX);
  }

  public int bearingY() {
    return data.readChar(Offset.BearingY);
  }

  public int advance() {
    return data.readByte(Offset.Advance);
  }

  public static class Builder extends GlyphMetrics.Builder<SmallGlyphMetrics> {

    protected Builder(WritableFontData data) {
      super(data);
    }

    protected Builder(ReadableFontData data) {
      super(data);
    }

    public int height() {
      return internalReadData().readByte(Offset.height);
    }

    public void setHeight(byte height) {
      internalWriteData().writeByte(Offset.height, height);
    }

    public int width() {
      return internalReadData().readByte(Offset.width);
    }

    public void setWidth(byte width) {
      internalWriteData().writeByte(Offset.width, width);
    }

    public int bearingX() {
      return internalReadData().readChar(Offset.BearingX);
    }

    public void setBearingX(byte bearing) {
      internalWriteData().writeChar(Offset.BearingX, bearing);
    }

    public int bearingY() {
      return internalReadData().readChar(Offset.BearingY);
    }

    public void setBearingY(byte bearing) {
      internalWriteData().writeChar(Offset.BearingY, bearing);
    }

    public int advance() {
      return internalReadData().readByte(Offset.Advance);
    }

    public void setAdvance(byte advance) {
      internalWriteData().writeByte(Offset.Advance, advance);
    }

    @Override
    protected SmallGlyphMetrics subBuildTable(ReadableFontData data) {
      return new SmallGlyphMetrics(data);
    }

    @Override
    protected void subDataSet() {
      // NOP
    }

    @Override
    protected int subDataSizeToSerialize() {
      return 0;
    }

    @Override
    protected boolean subReadyToSerialize() {
      return false;
    }

    @Override
    protected int subSerialize(WritableFontData newData) {
      return data().copyTo(newData);
    }
  }
}
