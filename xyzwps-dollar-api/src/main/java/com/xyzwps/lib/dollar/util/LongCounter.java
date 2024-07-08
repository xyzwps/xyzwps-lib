package com.xyzwps.lib.dollar.util;

/**
 * A simple counter for long values.
 */
public class LongCounter {
    private long count;
    private final long init;

    /**
     * Creates a new LongCounter with the specified initial value.
     *
     * @param init the initial value
     */
    public LongCounter(long init) {
        this.count = this.init = init;
    }

    /**
     * Get the current value and increment it by 1.
     *
     * @return the previous value
     */
    public long getAndIncr() {
        return count++;
    }

    /**
     * Reset the counter to the initial value.
     */
    public void reset() {
        this.count = this.init;
    }
}
