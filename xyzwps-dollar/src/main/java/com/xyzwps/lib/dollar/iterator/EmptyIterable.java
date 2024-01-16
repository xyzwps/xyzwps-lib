package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;

public enum EmptyIterable implements Iterable<Object> {
    INSTANCE;

    @Override
    public Iterator<Object> iterator() {
        return EmptyIterator.create();
    }

    @SuppressWarnings("unchecked")
    public static <T> Iterable<T> create() {
        return (Iterable<T>) INSTANCE;
    }
}
