package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.util.Counter;
import com.xyzwps.lib.dollar.util.ObjIntFunction;

import java.util.Iterator;
import java.util.function.Function;

record Map2Iterator<T, R>(Iterator<T> up, ObjIntFunction<T, R> mapper, Counter counter) implements Iterator<R> {

    @Override
    public boolean hasNext() {
        return up.hasNext();
    }

    @Override
    public R next() {
        return mapper.apply(up.next(), counter.getAndIncr());
    }
}
