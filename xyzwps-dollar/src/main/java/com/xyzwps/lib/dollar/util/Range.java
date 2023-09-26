package com.xyzwps.lib.dollar.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.IntConsumer;

public final class Range implements Iterable<Integer> {

    /**
     * Include
     */
    private final int start;

    /**
     * Exclude
     */
    private final int end;

    public Range(int start, int end) {
        if (start >= end) {
            throw new IllegalArgumentException("The start value should be less than end value.");
        }

        this.start = start;
        this.end = end;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            private int current = start;

            @Override
            public boolean hasNext() {
                return current < end;
            }

            @Override
            public Integer next() {
                if (hasNext()) {
                    return current++;
                }
                throw new NoSuchElementException();
            }
        };
    }

    public void forEachInt(IntConsumer consumer) {
        Objects.requireNonNull(consumer);
        for (int i = start; i < end; i++) {
            consumer.accept(i);
        }
    }
}
