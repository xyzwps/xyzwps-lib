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

   public static void main(String[] args) {
        var t = infinite(0)
                .filter(i -> {
                    System.out.printf("=> filter: %d > 1\n", i);
                    return i > 1;
                })
                .map(i -> {
                    System.out.printf("=> map: %d + 4 = %d\n", i, i + 4);
                    return i + 4;
                })
                .take(2);
        System.out.println("============");
        t.forEach(it -> System.out.printf("=> forEach %d \n", it));
        System.out.println("============");
    }

}