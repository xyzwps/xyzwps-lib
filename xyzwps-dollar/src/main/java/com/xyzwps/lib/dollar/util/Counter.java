package com.xyzwps.lib.dollar.util;

public final class Counter {
    private int count = 0;
    private final int init;

    public Counter(int init) {
        this.count = init;
        this.init = init;
    }

    public int getAndIncr() {
        return count++;
    }

    public int incrAndGet() {
        return ++count;
    }

    public int get() {
        return count;
    }

    public void reset() {
        this.count = this.init;
    }
}