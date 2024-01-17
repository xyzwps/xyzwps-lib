package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;

class SkipIterator<T> implements Iterator<T> {

    private final Iterator<T> up;
    private final int n;

    SkipIterator(Iterator<T> up, int n) {
        this.up = up;
        this.n = n;
    }

    private boolean skipped = false;

    void tryToSkip() {
        if (skipped) return;

        for (int i = 0; i < n; i++) {
            if (up.hasNext()) {
                up.next();
            } else {
                break;
            }
        }
        this.skipped = true;
    }

    @Override
    public boolean hasNext() {
        tryToSkip();
        return up.hasNext();
    }

    @Override
    public T next() {
        tryToSkip();
        return up.next();
    }
}
