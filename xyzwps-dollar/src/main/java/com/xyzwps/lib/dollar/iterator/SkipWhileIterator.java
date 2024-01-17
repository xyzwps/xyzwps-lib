package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

class SkipWhileIterator<T> implements Iterator<T> {

    private final Iterator<T> up;
    private final Predicate<T> predicate;

    SkipWhileIterator(Iterator<T> up, Predicate<T> predicate) {
        this.up = up;
        this.predicate = predicate;
    }

    sealed interface Stage {
        enum SkipStage implements Stage {
            INSTANCE
        }

        record FindFirstStage(Object value) implements Stage {
        }

        enum LastStage implements Stage {
            INSTANCE
        }
    }

    private Stage stage = Stage.SkipStage.INSTANCE;


    void tryToSkip() {
        if (stage instanceof Stage.SkipStage) {
            while (up.hasNext()) {
                var next = up.next();
                if (!predicate.test(next)) {
                    stage = new Stage.FindFirstStage(next);
                    return;
                }
            }
        }
    }

    @Override
    public boolean hasNext() {
        tryToSkip();
        return switch (stage) {
            case Stage.SkipStage ignored -> false; // all skipped
            case Stage.FindFirstStage value -> true;
            case Stage.LastStage ignored -> up.hasNext();
        };
    }

    @Override
    public T next() {
        tryToSkip();
        return switch (stage) {
            case Stage.SkipStage ignored -> throw new NoSuchElementException(); // all skipped
            case Stage.FindFirstStage value -> {
                stage = Stage.LastStage.INSTANCE;
                yield (T) value.value;
            }
            case Stage.LastStage ignored -> up.next();
        };
    }
}
