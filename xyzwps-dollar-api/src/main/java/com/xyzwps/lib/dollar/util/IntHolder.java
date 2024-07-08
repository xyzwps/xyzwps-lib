package com.xyzwps.lib.dollar.util;

/**
 * A simple holder for an integer value.
 */
public final class IntHolder {

    private int value;

    /**
     * Creates a new IntHolder with the specified value.
     *
     * @param value the initial value
     */
    public IntHolder(int value) {
        this.value = value;
    }

    /**
     * Increments the value by 1 and returns the new value.
     *
     * @return the new value
     */
    public int incrAndGet() {
        return ++this.value;
    }

    /**
     * Get the current value.
     *
     * @return the current value
     */
    public int get() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value the new value
     */
    public void set(int value) {
        this.value = value;
    }

    /**
     * Add the specified value to the current value.
     *
     * @param value the value to add
     */
    public void add(int value) {
        this.value += value;
    }
}
