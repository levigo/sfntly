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
import org.jadice.font.sfntly.table.core.HorizontalMetricsTable;
import java.util.ArrayList;
import java.util.List;

/** @author Raph Levien */
public class HorizontalMetricsTableSubsetter extends TableSubsetterImpl {

  protected HorizontalMetricsTableSubsetter() {
    // Note: doesn't actually create the hhea table, that should be done in the
    // setUpTables method of the invoking subsetter.
    super(Tag.hmtx, Tag.hhea);
  }

  @Override
  public boolean subset(Subsetter subsetter, Font font, Font.Builder fontBuilder) {
    List<Integer> permutationTable = subsetter.glyphMappingTable();
    if (permutationTable == null) {
      return false;
    }
    HorizontalMetricsTable origMetrics = font.getTable(Tag.hmtx);
    List<HorizontalMetricsTableBuilder.LongHorMetric> metrics = new ArrayList<>();
    for (int origGlyphId : permutationTable) {
      int advanceWidth = origMetrics.advanceWidth(origGlyphId);
      int lsb = origMetrics.leftSideBearing(origGlyphId);
      metrics.add(new HorizontalMetricsTableBuilder.LongHorMetric(advanceWidth, lsb));
    }
    new HorizontalMetricsTableBuilder(fontBuilder, metrics).build();
    return true;
  }
}
