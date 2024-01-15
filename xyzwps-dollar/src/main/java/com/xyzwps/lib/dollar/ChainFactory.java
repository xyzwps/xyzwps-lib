package com.xyzwps.lib.dollar;

public interface ChainFactory {

    <T> Chain<T> empty();

    <T> Chain<T> from(Iterable<T> iterable);

    @SuppressWarnings("unchecked")
    <T> Chain<T> just(T... elements);

    Chain<Long> infinite(long start);

    Chain<Integer> range(int start, int end);
}
