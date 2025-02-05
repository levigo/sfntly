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
 * A fixed size memory implementation of the ByteArray interface.
 *
 * @author Stuart Gill
 */
final class MemoryByteArray extends ByteArray {

  private byte[] b;

  /**
   * Construct a new MemoryByteArray with a new array of the size given. It is assumed that none of
   * the array is filled and readable.
   *
   * @param length the length to make the storage array
   */
  public MemoryByteArray(int length) {
    this(new byte[length], 0);
  }

  /**
   * Construct a new MemoryByteArray to wrap the actual underlying byte array. This MemoryByteArray
   * takes ownership of the array after construction and it should not be used outside of this
   * object. It is assumed that the entire array is filled and readable.
   *
   * @param b the byte array that provides the actual storage
   */
  public MemoryByteArray(byte[] b) {
    this(b, b.length);
  }

  /**
   * Construct a new MemoryByteArray to wrap the actual underlying byte array. This MemoryByteArray
   * takes ownership of the array after construction and it should not be used outside of this
   * object.
   *
   * @param b the byte array that provides the actual storage
   * @param filledLength the index of the last byte in the array has data
   * @throws IndexOutOfBoundsException if the given bounds don't fit within the byte array given
   */
  public MemoryByteArray(byte[] b, int filledLength) {
    super(filledLength, b.length);
    this.b = b;
  }

  @Override
  protected void internalPut(int index, byte b) {
    this.b[index] = b;
  }

  @Override
  protected int internalPut(int index, byte[] b, int offset, int length) {
    System.arraycopy(b, offset, this.b, index, length);
    return length;
  }

  @Override
  protected int internalGet(int index) {
    return b[index];
  }

  @Override
  protected int internalGet(int index, byte[] b, int offset, int length) {
    System.arraycopy(this.b, index, b, offset, length);
    return length;
  }

  @Override
  public void close() {

    this.b = null;
  }

  @Override
  public int copyTo(OutputStream os, int offset, int length) throws IOException {
    os.write(b, offset, length);
    return length;
  }
}
