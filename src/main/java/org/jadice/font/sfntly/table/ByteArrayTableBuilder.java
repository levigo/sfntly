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

package org.jadice.font.sfntly.table;

import org.jadice.font.sfntly.data.WritableFontData;
import org.jadice.font.sfntly.data.ReadableFontData;

import java.io.IOException;

/**
 * An abstract builder base for byte array based tables.
 *
 * @author Stuart Gill
 */
public abstract class ByteArrayTableBuilder<T extends Table> extends TableBasedTableBuilder<T> {

  protected ByteArrayTableBuilder(Header header, WritableFontData data) {
    super(header, data);
  }

  protected ByteArrayTableBuilder(Header header, ReadableFontData data) {
    super(header, data);
  }

  /**
   * Get the byte value at the specified index.
   *
   * @param index index relative to the start of the table
   */
  public int byteValue(int index) throws IOException {
    ReadableFontData data = internalReadData();
    if (data == null) {
      throw new IOException("No font data for the table.");
    }
    return data.readByte(index);
  }

  /**
   * Set the byte value at the specified index.
   *
   * @param index index relative to the start of the table
   */
  public void setByteValue(int index, byte b) throws IOException {
    WritableFontData data = internalWriteData();
    if (data == null) {
      throw new IOException("No font data for the table.");
    }
    data.writeByte(index, b);
  }

  /** Get the number of bytes set for this table. It may include padding bytes at the end. */
  public int byteCount() throws IOException {
    ReadableFontData data = internalReadData();
    if (data == null) {
      throw new IOException("No font data for the table.");
    }
    return data.length();
  }
}
