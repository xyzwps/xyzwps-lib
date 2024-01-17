package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;

import static com.xyzwps.lib.dollar.util.ObjectUtils.defaultTo;

public class LazyIterator<T> implements Iterator<T> {

    private final Supplier<Iterator<T>> supplier;

    public LazyIterator(Supplier<Iterator<T>> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }

    private Iterator<T> iterator;

    void useIterator() {
        if (iterator == null) {
            iterator = defaultTo(supplier.get(), EmptyIterator.create());
        }
    }

    @Override
    public boolean hasNext() {
        useIterator();
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }
}
