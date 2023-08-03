package com.xyzwps.lib.dollar.sub;

import java.util.Objects;

public class SafeObserver<T> implements Observer<T> {

    private volatile boolean closed = false; // TODO: 搞清楚 volatile，不行的话，就用 atomic boolean

    private final Observer<T> destination;

    public SafeObserver(Observer<T> destination) {
        this.destination = Objects.requireNonNull(destination);
    }

    @Override
    public void next(T value) {
        if (!this.closed) {
            this.destination.next(value);
        }
    }

    @Override
    public void error(Exception ex) {
        if (!this.closed) {
            this.closed = true;
            this.destination.error(ex);
        }
    }

    @Override
    public void complete() {
        if (!this.closed) {
            this.closed = true;
            this.destination.complete();
        }
    }
}
