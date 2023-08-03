package com.xyzwps.lib.dollar.sub;

import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.function.Function;

/**
 * See
 * <ul>
 *  <li><a href="https://benlesh.com/posts/learning-observable-by-building-observable/">Learning Observable By Building Observable</a></li>
 *  <li><a href="https://benlesh.medium.com/learning-observable-by-building-observable-d5da57405d87">Learning Observable By Building Observable</a></li>
 * </ul>
 * @param <T>
 */
public class Observable<T> {

    private final Function<Observer<T>, Runnable> wrapper;

    public Observable(Function<Observer<T>, Runnable> wrapper) {
        this.wrapper = Objects.requireNonNull(wrapper);
    }

    public Subscription subscribe(Observer<T> observer) {
        var subscription = new Subscription();
        var safeObserver = new SafeObserver<>(observer);
        subscription.add(this.wrapper.apply(safeObserver));
        return subscription;
    }
}
