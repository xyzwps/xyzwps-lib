package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.util.ObjectHolder;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public class FilterIterator<T> implements Iterator<T> {

    private final Predicate<T> predicate;
    private final Iterator<T> from;

    public FilterIterator(Iterator<T> from, Predicate<T> predicate) {
        this.from = Objects.requireNonNull(from);
        this.predicate = Objects.requireNonNull(predicate);
    }

    private ObjectHolder<T> next = null;

    private void tryFindNext() {
        if (next != null) return;

        while (from.hasNext()) {
            var next = from.next();
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
