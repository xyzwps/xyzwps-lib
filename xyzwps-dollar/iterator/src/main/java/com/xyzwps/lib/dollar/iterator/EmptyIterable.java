package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;

/**
 * Iterate nothing.
 */
public enum EmptyIterable implements Iterable<Object> {
    /**
     * Singleton.
     */
    INSTANCE;

    @Override
    public Iterator<Object> iterator() {
        return EmptyIterator.create();
    }

    /**
     * Create a new empty iterator.
     *
     * @param <T> element type. Haha
     * @return new empty iterator
     */
    @SuppressWarnings("unchecked")
    public static <T> Iterable<T> create() {
        return (Iterable<T>) INSTANCE;
    }
}
