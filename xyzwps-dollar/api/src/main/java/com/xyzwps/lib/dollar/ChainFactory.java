package com.xyzwps.lib.dollar;

/**
 * The ChainFactory interface provides a set of methods for creating Chain instances.
 * It is designed to work with any type of items, as specified by the generic parameter T.
 * <p>
 * The interface includes methods for creating Chain instances from various sources such as:
 * <ul>
 *  <li>an empty collection</li>
 *  <li>an iterable</li>
 *  <li>an array of elements</li>
 *  <li>a range of integers</li>
 *  <li>an infinite sequence of integers</li>
 * </ul>
 * <p>
 * Each method in this interface returns a Chain, allowing the operations to be chained together.
 * This design enables a functional programming style, where transformations on the collection
 * can be expressed as a sequence of operations.
 */
public interface ChainFactory {

    /**
     * Creates an empty Chain.
     *
     * @param <T> the type of items in the Chain
     * @return an empty Chain
     */
    <T> Chain<T> empty();

    /**
     * Creates a Chain from an iterable.
     *
     * @param iterable the iterable to create the Chain from
     * @param <T>      the type of items in the Chain
     * @return a Chain containing the elements of the iterable
     */
    <T> Chain<T> from(Iterable<T> iterable);

    /**
     * Creates a Chain from an array of elements.
     *
     * @param elements the elements to create the Chain from
     * @param <T>      the type of items in the Chain
     * @return a Chain containing the elements of the array
     */
    @SuppressWarnings("unchecked")
    <T> Chain<T> just(T... elements);

    /**
     * Creates an infinite Chain of integers starting from the specified value.
     *
     * @param start the starting value
     * @return an infinite Chain of integers
     */
    Chain<Integer> infinite(int start);

    /**
     * Creates a Chain of integers in the specified range.
     *
     * @param start the starting value (inclusive)
     * @param end   the ending value (exclusive)
     * @return a Chain of integers in the specified range
     */
    Chain<Integer> range(int start, int end);
}
