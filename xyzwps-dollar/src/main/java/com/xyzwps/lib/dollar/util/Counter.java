package com.xyzwps.lib.dollar.util;

/**
 * Very simple int counter.
 */
public final class Counter {
    private int count;
    private final int init;

    /**
     * Constructor.
     *
     * @param init init value
     */
    public Counter(int init) {
        this.count = this.init = init;
    }

    /**
     * Return current value and then increment it.
     *
     * @return current value before incrementing
     */
    public int getAndIncr() {
        return count++;
    }

    /**
     * Increment current value and then return new value.
     *
     * @return incremented value
     */
    public int incrAndGet() {
        return ++count;
    }

    /**
     * Get current value.
     *
     * @return current value
     */
    public int get() {
        return count;
    }

    /**
     * Reset to init value.
     */
    public void reset() {
        this.count = this.init;
    }
}