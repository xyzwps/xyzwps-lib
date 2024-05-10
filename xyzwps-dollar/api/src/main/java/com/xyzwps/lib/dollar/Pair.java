package com.xyzwps.lib.dollar;

import java.util.Objects;

/**
 * A simple tuple with two elements. Null elements are acceptable.
 * It can also represent the key-value pair.
 *
 * @param <F> first element type
 * @param <S> second element type
 */
public final class Pair<F, S> {

    private final F first;
    private final S second;

    /**
     * Default constructor.
     *
     * @param first  first element
     * @param second second element
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Get the first element of the tuple.
     *
     * @return first element
     */
    public F first() {
        return first;
    }

    /**
     * Get the second element of the tuple.
     *
     * @return second element
     */
    public S second() {
        return second;
    }

    /**
     * Get the left element of the tuple.
     * <p>
     * Alias for {@link #first()}
     *
     * @return first element
     */
    public F left() {
        return first;
    }

    /**
     * Get the right element of the tuple.
     * <p>
     * Alias for {@link #second()}
     *
     * @return second element
     */
    public S right() {
        return second;
    }

    /**
     * Get the key of pair.
     * <p>
     * Alias for {@link #first()}
     *
     * @return key of pair
     */
    public F key() {
        return first;
    }

    /**
     * Get the value of pair.
     * <p>
     * Alias for {@link #second()}
     *
     * @return value of pair
     */
    public S value() {
        return second;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj instanceof Pair) {
            Pair<?, ?> p = (Pair<?, ?>) obj;
            return Objects.equals(this.first, p.first) && Objects.equals(this.second, p.second);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }


    @Override
    public String toString() {
        return "(" + this.first + ", " + this.second + ")";
    }

    /**
     * Pair factory method.
     *
     * @param first  first element
     * @param second second element
     * @param <F>    first element type
     * @param <S>    second element type
     * @return new pair
     */
    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }
}
