package com.xyzwps.lib.dollar.util;

/**
 * A type of triple arguments function.
 *
 * @param <A1> first argument type
 * @param <A2> second argument type
 * @param <A3> third argument type
 * @param <R>  function return type
 */
public interface Function3<A1, A2, A3, R> {

    /**
     * Function caller.
     *
     * @param arg1 first argument
     * @param arg2 second argument
     * @param args third argument
     * @return function return result
     */
    R apply(A1 arg1, A2 arg2, A3 args);
}
