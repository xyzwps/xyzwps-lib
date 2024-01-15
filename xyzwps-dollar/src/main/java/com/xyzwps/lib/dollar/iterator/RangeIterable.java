package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;

public class RangeIterable implements Iterable<Integer> {

    private final int start;
    private final int end;
    private final boolean incr;

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
