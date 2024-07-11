package com.xyzwps.lib.dollar.util;

/**
 * Utilities for creating functions.
 */
public final class Functions {

    /**
     * Do nothing consumer.
     *
     * @param t   the consumed
     * @param <T> type of the consumed
     */
    public static <T> void consumeNothing(T t) {
    }

    private Functions() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
