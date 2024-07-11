package com.xyzwps.lib.dollar;

/**
 * {@link ObjIntPredicate} represents a function accepting two arguments,
 * the first one is an object and the second is an int primitive value.
 * This function returns a boolean value.
 *
 * @param <E> the first argument type of the predicate
 */
@FunctionalInterface
public interface ObjIntPredicate<E> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param element the first argument
     * @param index   the second argument
     * @return {@code true} if the arguments matches the predicate, otherwise {@code false}
     */
    boolean test(E element, int index);
}
