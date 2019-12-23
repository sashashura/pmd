/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.util.document;

import net.sourceforge.pmd.internal.util.AssertionUtil;

/**
 * Immutable implementation of the {@link TextRegion} interface.
 */
class TextRegionImpl implements TextRegion {

    private final int startOffset;
    private final int length;

    TextRegionImpl(int offset, int length) {
        this.startOffset = AssertionUtil.requireNonNegative("Start offset", offset);
        this.length = AssertionUtil.requireNonNegative("Region length", length);
    }

    @Override
    public int getStartOffset() {
        return startOffset;
    }

    @Override
    public int getEndOffset() {
        return startOffset + length;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "Region(start=" + startOffset + ", len=" + length + ")";
    }

    static final class WithLineInfo extends TextRegionImpl implements RegionWithLines {

        private final int beginLine;
        private final int endLine;
        private final int beginColumn;
        private final int endColumn;

        WithLineInfo(int startOffset, int length, int beginLine, int beginColumn, int endLine, int endColumn) {
            super(startOffset, length);
            this.beginLine = AssertionUtil.requireOver1("Begin line", beginLine);
            this.endLine = AssertionUtil.requireOver1("End line", endLine);
            this.beginColumn = AssertionUtil.requireOver1("Begin column", beginColumn);
            this.endColumn = AssertionUtil.requireOver1("End column", endColumn);

            requireLinesCorrectlyOrdered();
        }

        private void requireLinesCorrectlyOrdered() {
            if (beginLine > endLine) {
                throw AssertionUtil.mustBe("endLine", endLine, ">= beginLine (= " + beginLine + ")");
            } else if (beginLine == endLine && beginColumn > endColumn) {
                throw AssertionUtil.mustBe("endColumn", endColumn, ">= beginColumn (= " + beginColumn + ")");
            }
        }

        @Override
        public int getBeginLine() {
            return beginLine;
        }

        @Override
        public int getEndLine() {
            return endLine;
        }

        @Override
        public int getBeginColumn() {
            return beginColumn;
        }

        @Override
        public int getEndColumn() {
            return endColumn;
        }


    }

}
