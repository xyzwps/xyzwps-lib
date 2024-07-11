package com.xyzwps.lib.dollar.util;

/**
 * A simple holder for an object value.
 *
 * @param <T> the type of the value
 */
public class ObjectHolder<T> {
    private T value;

    /**
     * Creates a new ObjectHolder with the specified value.
     *
     * @param value the initial value
     */
    public ObjectHolder(T value) {
        this.value = value;
    }

    /**
     * Get the current value.
     *
     * @return the current value
     */
    public T value() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value the new value
     */
    public void set(T value) {
        this.value = value;
    }
}