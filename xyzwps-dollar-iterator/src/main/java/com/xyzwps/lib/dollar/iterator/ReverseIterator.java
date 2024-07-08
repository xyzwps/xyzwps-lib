package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.util.ArrayListReverseIterator;

import java.util.ArrayList;
import java.util.Iterator;

class ReverseIterator<T> implements Iterator<T> {

    private final Iterator<T> up;

    ReverseIterator(Iterator<T> up) {
        this.up = up;
    }

    private Iterator<T> reversed;

    private void tryToReverse() {
        if (reversed != null) return;

        var list = new ArrayList<T>();
        while (up.hasNext()) {
            list.add(up.next());
        }
        this.reversed = new ArrayListReverseIterator<>(list);
    }


    @Override
    public boolean hasNext() {
        this.tryToReverse();
        return reversed.hasNext();
    }

    @Override
    public T next() {
        this.tryToReverse();
        return reversed.next();
    }
}
