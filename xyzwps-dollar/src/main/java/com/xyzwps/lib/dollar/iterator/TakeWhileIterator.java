package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class TakeWhileIterator<T> implements Iterator<T> {

    private final Iterator<T> up;
    private final Predicate<T> predicate;

    TakeWhileIterator(Iterator<T> up, Predicate<T> predicate) {
        this.up = up;
        this.predicate = predicate;
    }

    private T next = null;
    private Stage stage = Stage.WANT;

    private enum Stage {
        WANT,
        TAKEN,
        NO_MORE
    }

    private void tryToTakeNext() {
        if (stage != Stage.WANT) return;

        if (up.hasNext()) {
            var next = up.next();
            if (predicate.test(next)) {
                this.next = next;
                this.stage = Stage.TAKEN;
            } else {
                this.stage = Stage.NO_MORE;
            }
        } else {
            stage = Stage.NO_MORE;
        }
    }

    @Override
    public boolean hasNext() {
        tryToTakeNext();
        return stage == Stage.TAKEN;
    }

    @Override
    public T next() {
        tryToTakeNext();
        return switch (stage) {
            case WANT -> throw new IllegalStateException();
            case NO_MORE -> throw new NoSuchElementException();
            case TAKEN -> {
                this.stage = Stage.WANT;
                yield this.next;
            }
        };
    }
}
