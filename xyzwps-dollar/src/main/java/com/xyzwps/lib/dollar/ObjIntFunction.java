package com.xyzwps.lib.dollar;

/**
 * {@link ObjIntFunction} represents a function accepting two arguments,
 * the first one is an object and the second is an int primitive value.
 *
 * @param <T> first argument type of the function
 * @param <R> return type of the function
 */
@FunctionalInterface
public interface ObjIntFunction<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param element the first argument
     * @param index   the second argument
     * @return the function return value
     */
    R apply(T element, int index);
}
