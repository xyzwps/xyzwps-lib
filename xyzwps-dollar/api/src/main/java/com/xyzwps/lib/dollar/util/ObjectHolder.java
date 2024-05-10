package com.xyzwps.lib.dollar.util;

public class ObjectHolder<T> {
    private T value;

    public ObjectHolder(T value) {
        this.value = value;
    }

    public T value() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}