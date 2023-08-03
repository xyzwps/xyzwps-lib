package com.xyzwps.lib.dollar.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An empty iterator. You can get nothing here.
 *
 * @param <T> element type. Haha.
 */
public class EmptyIterator<T> implements Iterator<T> {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }

    private EmptyIterator() {
    }

    /**
     * Create an empty iterator.
     *
     * @param <T> element type
     * @return an empty iterator
     */
    public static <T> Iterator<T> create() {
        return new EmptyIterator<>();
    }
}