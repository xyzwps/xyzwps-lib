package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class MapIterator<T, R> implements Iterator<R> {

    private final Function<T, R> mapper;
    private final Iterator<T> from;

    public MapIterator(Iterator<T> from, Function<T, R> mapper) {
        this.from = Objects.requireNonNull(from);
        this.mapper = Objects.requireNonNull(mapper);
    }


    @Override
    public boolean hasNext() {
        return from.hasNext();
    }

    @Override
    public R next() {
        return mapper.apply(from.next());
    }
}
