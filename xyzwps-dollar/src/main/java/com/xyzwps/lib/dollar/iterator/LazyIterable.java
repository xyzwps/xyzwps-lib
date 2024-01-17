package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.function.Supplier;

class LazyIterable<T> implements Iterable<T> {

    private final Supplier<Iterable<T>> supplier;

    LazyIterable(Supplier<Iterable<T>> supplier) {
        this.supplier = supplier;
    }

    private Iterable<T> iterable;

    @Override
    public Iterator<T> iterator() {
        if (iterable == null) {
            iterable = supplier.get();
        }
        return iterable.iterator();
    }
}
