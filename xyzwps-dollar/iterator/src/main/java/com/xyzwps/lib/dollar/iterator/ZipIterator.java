package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;

class ZipIterator<T, T2, R> implements Iterator<R> {
    private final Iterator<T> from1;
    private final Iterator<T2> from2;
    private final BiFunction<T, T2, R> zipper;

    public ZipIterator(Iterator<T> from1, Iterator<T2> from2, BiFunction<T, T2, R> zipper) {
        this.from1 = Objects.requireNonNull(from1);
        this.from2 = Objects.requireNonNull(from2);
        this.zipper = Objects.requireNonNull(zipper);
    }

    @Override
    public boolean hasNext() {
        return from1.hasNext() || from2.hasNext();
    }

    @Override
    public R next() {
        return zipper.apply(from1.hasNext() ? from1.next() : null, from2.hasNext() ? from2.next() : null);
    }
}
