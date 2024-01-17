package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.function.Function;

record MapIterator<T, R>(Iterator<T> up, Function<T, R> mapper) implements Iterator<R> {

    @Override
    public boolean hasNext() {
        return up.hasNext();
    }

    @Override
    public R next() {
        return mapper.apply(up.next());
    }
}
