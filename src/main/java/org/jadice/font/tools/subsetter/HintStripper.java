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

package org.jadice.font.tools.subsetter;

import org.jadice.font.sfntly.Font;
import org.jadice.font.sfntly.FontFactory;
import java.util.HashSet;
import java.util.Set;

/** @author Raph Levien */
public class HintStripper extends Subsetter {

  {
    Set<TableSubsetter> temp = new HashSet<>();
    temp.add(new GlyphTableStripper());
    tableSubsetters = temp;
  }

  public HintStripper(Font font, FontFactory fontFactory) {
    super(font, fontFactory);
  }
}
