package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;

public class InfiniteIterator implements Iterator<Long> {

    private long current;

    public InfiniteIterator(long start) {
        this.current = start;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Long next() {
        return this.current++;
    }
}
