package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.util.ObjectHolder;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

class FilterIterator<T> implements Iterator<T> {

    private final Predicate<T> predicate;
    private final Iterator<T> up;

    public FilterIterator(Iterator<T> up, Predicate<T> predicate) {
        this.up = up;
        this.predicate = predicate;
    }

    private ObjectHolder<T> next = null;

    private void tryFindNext() {
        if (next != null) return;

        while (up.hasNext()) {
            var next = up.next();
            if (predicate.test(next)) {
                this.next = new ObjectHolder<>(next);
                return;
            }
        }
    }

    @Override
    public boolean hasNext() {
        this.tryFindNext();
        return next != null;
    }

    @Override
    public T next() {
        this.tryFindNext();
        if (this.next == null) {
            throw new NoSuchElementException();
        }

        var value = this.next.value();
        this.next = null;
        return value;
    }
}
