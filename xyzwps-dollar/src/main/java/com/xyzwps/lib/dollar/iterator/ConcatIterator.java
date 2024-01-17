package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;

class ConcatIterator<T> implements Iterator<T> {

    private final Iterator<T> up;
    private final Iterable<T> downIterable;
    private Iterator<T> down;

    public ConcatIterator(Iterator<T> up, Iterable<T> downIterable) {
        this.up = up;
        this.downIterable = downIterable;
        this.down = null;
    }

    @Override
    public boolean hasNext() {
        if (up.hasNext()) {
            return true;
        }
        if (down == null) {
            down = downIterable.iterator();
        }
        return down.hasNext();
    }

    @Override
    public T next() {
        return down == null ? up.next() : down.next();
    }
}
