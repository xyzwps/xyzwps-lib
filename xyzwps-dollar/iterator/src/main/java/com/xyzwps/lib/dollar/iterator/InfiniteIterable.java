package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;

public class InfiniteIterable implements Iterable<Integer> {

    private final int start;

    public InfiniteIterable(int start) {
        this.start = start;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            private int current = start;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                return current++;
            }
        };
    }
}
