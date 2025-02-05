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

package org.jadice.font.sfntly.testutils;

import java.io.File;

/**
 * Holds TestFontNames enums which can be loaded into Fonts. Later, this will have support for
 * checking the fingerprints to make sure that the fonts haven't been changed.
 *
 * @author Vinay Shah
 */
public class TestFont {

  private static final String TESTDATA_PATH = "src/test/resources/";

  public enum TestFontNames {
    DROIDSANS("DroidSans-Regular.ttf"),
    OPENSANS("OpenSans-Regular.ttf"),
    ROBOTO("Roboto-Regular.ttf");

    private final String path;

    private TestFontNames(String path) {
      this.path = path;
    }

    public File getFile() {
      return new File(TESTDATA_PATH + path);
    }
  }
}
