package com.xyzwps.lib.dollar.util;

public class LongCounter {
    private long count;
    private final long init;

    public LongCounter(long init) {
        this.count = this.init = init;
    }

    public long getAndIncr() {
        return count++;
    }

    public void reset() {
        this.count = this.init;
    }
}
