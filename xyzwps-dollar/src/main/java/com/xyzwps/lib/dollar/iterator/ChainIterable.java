package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ChainIterable<T> implements Iterable<T> {

    private final Iterable<T> iterable;

    public ChainIterable(Iterable<T> iterable) {
        this.iterable = iterable == null ? EmptyIterator::create : iterable;
    }

    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }

    public <R> ChainIterable<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return this.chain(itr -> new MapIterator<>(itr, mapper));
    }

    public ChainIterable<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return this.chain(itr -> new FilterIterator<>(itr, predicate));
    }

    public ChainIterable<T> take(int n) {
        return this.chain(itr -> new TakeIterator<>(itr, n));
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        this.iterable.forEach(action);
    }

    private <R> ChainIterable<R> chain(Function<Iterator<T>, Iterator<R>> fn) {
        return new ChainIterable<>(() -> fn.apply(this.iterable.iterator()));
    }


    public static ChainIterable<Long> infinite(int start) {
        return new ChainIterable<>(() -> new InfiniteIterator(start));
    }

    public static <T> ChainIterable<T> empty() {
        return new ChainIterable<>(EmptyIterable.create());
    }
}
