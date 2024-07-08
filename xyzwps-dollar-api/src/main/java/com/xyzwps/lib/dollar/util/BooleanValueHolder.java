package com.xyzwps.lib.dollar.util;

/**
 * Hold a <code>boolean</code> value.
 */
public final class BooleanValueHolder {
    private boolean value;

    /**
     * Constructor
     *
     * @param value for initializing
     */
    public BooleanValueHolder(boolean value) {
        this.value = value;
    }

    /**
     * Get current value.
     *
     * @return current value
     */
    public boolean value() {
        return value;
    }

    /**
     * Set new value.
     *
     * @param value new value
     */
    public void set(boolean value) {
        this.value = value;
    }
}
