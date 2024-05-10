package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

class TakeIterator<T> implements Iterator<T> {
    private final int n;
    private final Iterator<T> from;
    private int current = 0;

    public TakeIterator(Iterator<T> from, int n) {
        this.from = Objects.requireNonNull(from);
        this.n = n;
    }

    @Override
    public boolean hasNext() {
        return current < n && from.hasNext();
    }

    @Override
    public T next() {
        if (this.hasNext()) {
            this.current++;
            return from.next();
        } else {
            throw new NoSuchElementException();
        }
    }
}
