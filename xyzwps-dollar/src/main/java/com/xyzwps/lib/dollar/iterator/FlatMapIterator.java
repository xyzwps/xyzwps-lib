package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.function.Function;

class FlatMapIterator<T, R> implements Iterator<R> {

    private final Function<T, Iterable<R>> flat;
    private final Iterator<T> up;

    FlatMapIterator(Iterator<T> up, Function<T, Iterable<R>> flat) {
        this.flat = flat;
        this.up = up;
    }

    private Iterator<R> iterator;

    private void tryToNext() {
        while (iterator == null || !iterator.hasNext()) {
            if (up.hasNext()) {
                var next = up.next();
                if (next == null) {
                    continue;
                }

                var iterable = flat.apply(next);
                if (iterable == null) {
                    continue;
                }

                var iterator = iterable.iterator();
                if (iterator.hasNext()) {
                    this.iterator = iterator;
                    return;
                }
            } else {
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        tryToNext();
        return iterator != null && iterator.hasNext();
    }

    @Override
    public R next() {
        return iterator.next();
    }
}
