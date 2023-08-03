package com.xyzwps.lib.dollar.util;

public final class BooleanValueHolder {
    private boolean value;

    public BooleanValueHolder(boolean value) {
        this.value = value;
    }

    public boolean value() {
        return value;
    }

    public void set(boolean value) {
        this.value = value;
    }
}
