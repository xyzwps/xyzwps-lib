package com.xyzwps.lib.dollar.util;

import com.xyzwps.lib.dollar.Dollar;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A lazy iterator that defers the creation of the underlying iterator until it is actually needed.
 *
 * @param <T> the type of elements in the iterator
 */
public class LazyIterator<T> implements Iterator<T> {

    private final Supplier<Iterator<T>> supplier;

    /**
     * Creates a new LazyIterator with the specified supplier.
     *
     * @param supplier the supplier for the underlying iterator
     */
    public LazyIterator(Supplier<Iterator<T>> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }

    private Iterator<T> iterator;

    void useIterator() {
        if (iterator == null) {
            iterator = Dollar.$.defaultTo(supplier.get(), EmptyIterator.create());
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
