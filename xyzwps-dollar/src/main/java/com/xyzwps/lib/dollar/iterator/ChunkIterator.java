package com.xyzwps.lib.dollar.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

record ChunkIterator<T>(Iterator<T> up, int size) implements Iterator<List<T>> {
    @Override
    public boolean hasNext() {
        return up.hasNext();
    }

    @Override
    public List<T> next() {
        if (!up.hasNext()) throw new NoSuchElementException();

        var list = new ArrayList<T>(size);
        for (int i = 0; i < size; i++) {
            if (up.hasNext()) {
                list.add(up.next());
            } else {
                break;
            }
        }
        return list;
    }
}
