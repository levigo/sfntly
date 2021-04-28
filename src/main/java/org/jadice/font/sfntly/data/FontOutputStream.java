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

package org.jadice.font.sfntly.data;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream for writing font data.
 *
 * @author Stuart Gill
 * @see FontInputStream
 */
public class FontOutputStream extends OutputStream {
  private final OutputStream out;
  private long position;

  /** @param os output stream to wrap */
  public FontOutputStream(OutputStream os) {
    out = os;
  }

  /**
   * Get the current position in the stream in bytes.
   *
   * @return the current position in bytes
   */
  public long position() {
    return position;
  }

  @Override
  public void write(int b) throws IOException {
    out.write(b);
    this.position++;
  }

  @Override
  public void write(byte[] b) throws IOException {
    write(b, 0, b.length);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    if (off < 0 || len < 0 || off + len < 0 || off + len > b.length) {
      throw new IndexOutOfBoundsException();
    }
    out.write(b, off, len);
    position += len;
  }

  /** Write a Char value. */
  public void writeChar(byte c) throws IOException {
    write(c);
  }

  /** Write a UShort value. */
  public void writeUShort(int us) throws IOException {
    write((byte) ((us >> 8) & 0xff));
    write((byte) (us & 0xff));
  }

  /** Write a Short value. */
  public void writeShort(int s) throws IOException {
    writeUShort(s);
  }

  /** Write a UInt24 value. */
  public void writeUInt24(int ui) throws IOException {
    write((byte) ((ui >> 16) & 0xff));
    write((byte) ((ui >> 8) & 0xff));
    write((byte) (ui & 0xff));
  }

  /** Write a ULong value. */
  public void writeULong(long ul) throws IOException {
    write((byte) ((ul >> 24) & 0xff));
    write((byte) ((ul >> 16) & 0xff));
    write((byte) ((ul >> 8) & 0xff));
    write((byte) (ul & 0xff));
  }

  /** Write a Long value. */
  public void writeLong(long l) throws IOException {
    writeULong(l);
  }

  /** Write a Fixed value. */
  public void writeFixed(int f) throws IOException {
    writeULong(f);
  }

  /** Write DateTime value. */
  public void writeDateTime(long date) throws IOException {
    writeULong(date >> 32);
    writeULong(date);
  }
}
