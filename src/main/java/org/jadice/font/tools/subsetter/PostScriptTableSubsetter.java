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
import org.jadice.font.sfntly.Tag;
import org.jadice.font.sfntly.table.core.PostScriptTable;
import java.util.ArrayList;
import java.util.List;

/** @author Raph Levien */
public class PostScriptTableSubsetter extends TableSubsetterImpl {

  protected PostScriptTableSubsetter() {
    super(Tag.post);
  }

  @Override
  public boolean subset(Subsetter subsetter, Font font, Font.Builder fontBuilder) {
    List<Integer> permutationTable = subsetter.glyphMappingTable();
    if (permutationTable == null) {
      return false;
    }
    PostScriptTableBuilder postBuilder = new PostScriptTableBuilder();
    PostScriptTable post = font.getTable(Tag.post);
    postBuilder.initV1From(post);
    if (post.version() == 0x10000 || post.version() == 0x20000) {
      List<String> names = new ArrayList<>();
      for (Integer glyphId : permutationTable) {
        names.add(post.glyphName(glyphId));
      }
      postBuilder.setNames(names);
    }
    fontBuilder.newTableBuilder(Tag.post, postBuilder.build());
    return true;
  }
}
