package com.xyzwps.lib.dollar.util;

import java.util.Iterator;

/**
 * An iterable that generates a range of integers.
 */
public class RangeIterable implements Iterable<Integer> {

    private final int start;
    private final int end;
    private final boolean incr;

    /**
     * Create a new range iterable.
     *
     * @param start the start value
     * @param end   the end value
     */
    public RangeIterable(int start, int end) {
        this.start = start;
        this.end = end;
        this.incr = end > start;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<>() {

            private int current = start;

            @Override
            public boolean hasNext() {
                return incr ? current < end : current > end;
            }

            @Override
            public Integer next() {
                return incr ? current++ : current--;
            }
        };
    }
}
