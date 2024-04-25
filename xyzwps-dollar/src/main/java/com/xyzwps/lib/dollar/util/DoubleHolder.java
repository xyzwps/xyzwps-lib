package com.xyzwps.lib.dollar.util;

/**
 * Hold a double value
 */
public final class DoubleHolder {
    private double value;

    /**
     * Constructor.
     *
     * @param value for initializing
     */
    public DoubleHolder(double value) {
        this.value = value;
    }

    /**
     * Get current value.
     *
     * @return current value.
     */
    public double get() {
        return value;
    }

    /**
     * Set new value
     *
     * @param value new value
     */
    public void set(double value) {
        this.value = value;
    }
}
