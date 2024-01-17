package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ArrayIterable<T> implements Iterable<T> {
    private final T[] array;

    public ArrayIterable(T[] array) {
        this.array = Objects.requireNonNull(array);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int current = 0;

            @Override
            public boolean hasNext() {
                return current < array.length;
            }

            @Override
            public T next() {
                if (this.hasNext()) {
                    return array[current++];
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }
}
