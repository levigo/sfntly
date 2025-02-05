package org.jadice.font.sfntly.table.opentype.component;

import org.jadice.font.sfntly.table.core.PostScriptTable;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;

public class GlyphGroup extends BitSet implements Iterable<Integer> {
  private static final long serialVersionUID = 1L;

  private boolean inverse = false;

  public GlyphGroup() {
    super();
  }

  GlyphGroup(int glyph) {
    super.set(glyph);
  }

  GlyphGroup(Collection<Integer> glyphs) {
    for (int glyph : glyphs) {
      super.set(glyph);
    }
  }

  static GlyphGroup inverseGlyphGroup(Collection<GlyphGroup> glyphGroups) {
    GlyphGroup result = new GlyphGroup();
    for (GlyphGroup glyphGroup : glyphGroups) {
      result.or(glyphGroup);
    }
    result.inverse = true;
    return result;
  }

  public void add(int glyph) {
    set(glyph);
  }

  void addAll(Collection<Integer> glyphs) {
    for (int glyph : glyphs) {
      super.set(glyph);
    }
  }

  void addAll(GlyphGroup other) {
    or(other);
  }

  void copyTo(Collection<Integer> target) {
    for (int i = nextSetBit(0); i >= 0; i = nextSetBit(i + 1)) {
      target.add(i);
    }
  }

  GlyphGroup intersection(GlyphGroup other) {
    GlyphGroup intersection = new GlyphGroup();
    if (inverse && !other.inverse) {
      intersection.or(other);
      intersection.andNot(this);
    } else if (other.inverse && !inverse) {
      intersection.or(this);
      intersection.andNot(other);
    } else if (other.inverse && inverse) {
      intersection.inverse = true;
      intersection.or(this);
      intersection.or(other);
    } else {
      intersection.or(this);
      intersection.and(other);
    }
    return intersection;
  }

  boolean contains(int glyph) {
    return get(glyph) ^ inverse;
  }

  @Override
  public int size() {
    return cardinality();
  }

  @Override
  public Iterator<Integer> iterator() {
    return new Iterator<Integer>() {
      int i = 0;

      @Override
      public boolean hasNext() {
        return nextSetBit(i) >= 0;
      }

      @Override
      public Integer next() {
        i = nextSetBit(i);
        return i++;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  public String toString() {
    return toString(null);
  }

  public String toString(PostScriptTable post) {
    StringBuilder sb = new StringBuilder();
    if (inverse) {
      sb.append("not-");
    }
    int glyphCount = size();
    if (glyphCount > 1) {
      sb.append("[ ");
    }
    for (int glyphId : this) {
      sb.append(glyphId);

      if (post != null) {
        String glyphName = post.glyphName(glyphId);
        if (glyphName != null) {
          sb.append("-");
          sb.append(glyphName);
        }
      }
      sb.append(" ");
    }
    if (glyphCount > 1) {
      sb.append("] ");
    }
    return sb.toString();
  }
}
