package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Iterate an array.
 *
 * @param <T> array element type
 */
public class ArrayIterable<T> implements Iterable<T> {
    private final T[] array;

    /**
     * Constructor.
     *
     * @param array not null
     */
    public ArrayIterable(T[] array) {
        this.array = Objects.requireNonNull(array);
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(array);
    }

    /**
     * Iterator over an array.
     *
     * @param <T> array element type
     */
    public static class ArrayIterator<T> implements Iterator<T> {
        private final T[] array;

        /**
         * Constructor.
         *
         * @param array to be iterated
         */
        public ArrayIterator(T[] array) {
            this.array = array;
        }

        private int current = 0;

        @Override
        public boolean hasNext() {
            return array != null && array.length != 0 && current < array.length;
        }

        @Override
        public T next() {
            if (this.hasNext()) {
                return array[current++];
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
