package com.xyzwps.lib.dollar.chain;

abstract class IteratorHolder<T> {
    boolean hasNext = false;
    private T next;

    void addNext(T next) {
        this.hasNext = true;
        this.next = next;
    }

    void clear() {
        this.hasNext = false;
        this.next = null;
    }

    T getNextAndClear() {
        T t = this.next;
        this.clear();
        return t;
    }

    abstract void tryToGetNext();
}
