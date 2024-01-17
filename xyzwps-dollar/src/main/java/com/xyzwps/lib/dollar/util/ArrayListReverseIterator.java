package com.xyzwps.lib.dollar.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Iterate over an array list in reverse order.
 *
 * @param <T> element type
 */
public final class ArrayListReverseIterator<T> implements Iterator<T> {

    private final ArrayList<T> list;

    private int index;

    public ArrayListReverseIterator(ArrayList<T> list) {
        this.list = Objects.requireNonNull(list);
        this.index = list.size();
    }

    @Override
    public boolean hasNext() {
        return index > 0;
    }

    @Override
    public T next() {
        if (hasNext()) {
            return list.get(--index);
        }
        throw new NoSuchElementException();
    }
}
