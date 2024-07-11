package com.xyzwps.lib.dollar.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An empty iterator. You can get nothing here.
 */
public enum EmptyIterator implements Iterator<Object> {
    /**
     * Singleton.
     */
    INSTANCE;

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        throw new NoSuchElementException();
    }


    /**
     * Create an empty iterator.
     *
     * @param <T> element type
     * @return an empty iterator
     */
    @SuppressWarnings("unchecked")
    public static <T> Iterator<T> create() {
        return (Iterator<T>) INSTANCE;
    }
}