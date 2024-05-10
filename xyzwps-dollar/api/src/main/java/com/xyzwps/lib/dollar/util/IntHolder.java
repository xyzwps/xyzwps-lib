package com.xyzwps.lib.dollar.util;

public final class IntHolder {

    private int value;

    public IntHolder(int value) {
        this.value = value;
    }

    public int incrAndGet() {
        return ++this.value;
    }

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }

    public void add(int value) {
        this.value += value;
    }

    public void addMin(int value) {
        if (value < this.value) {
            this.value = value;
        }
    }

    public void addMax(int value) {
        if (value > this.value) {
            this.value = value;
        }
    }
}
