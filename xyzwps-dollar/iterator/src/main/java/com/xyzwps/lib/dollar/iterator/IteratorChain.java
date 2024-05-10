package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.Chain;
import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.MapEntryChain;
import com.xyzwps.lib.dollar.util.Counter;
import com.xyzwps.lib.dollar.util.ObjIntFunction;
import com.xyzwps.lib.dollar.util.ObjIntPredicate;

import java.util.*;
import java.util.function.*;

public class IteratorChain<T> implements Chain<T> {

    private final Iterable<T> iterable;

    public IteratorChain(Iterable<T> iterable) {
        this.iterable = iterable == null ? EmptyIterable.create() : iterable;
    }

    public static <T, R> IteratorChain<R> nest(Iterable<T> up, Function<Iterator<T>, Iterator<R>> grow) {
        return new IteratorChain<>(() -> grow.apply(up.iterator()));
    }

    @Override
    public Chain<List<T>> chunk(int chunkSize) {
        if (chunkSize < 1) throw new IllegalArgumentException();
        return nest(iterable, itr -> new ChunkIterator<>(itr, chunkSize));
    }

    @Override
    public Chain<T> concat(Iterable<T> tail) {
        if (tail == null) return this;
        return nest(iterable, itr -> new ConcatIterator<>(itr, tail));
    }

    @Override
    public Chain<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return nest(iterable, itr -> new FilterIterator<>(itr, predicate));
    }

    @Override
    public Chain<T> filter(ObjIntPredicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return nest(iterable, itr -> new Filter2Iterator<>(itr, predicate));
    }

    @Override
    public Optional<T> first() {
        var itr = iterable.iterator();
        return itr.hasNext() ? Optional.ofNullable(itr.next()) : Optional.empty();
    }

    @Override
    public <R> Chain<R> flatMap(Function<T, Iterable<R>> flatter) {
        Objects.requireNonNull(flatter);
        return nest(iterable, itr -> new FlatMapIterator<>(itr, flatter));
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        iterable.forEach(consumer);
    }

    @Override
    public void forEach(ObjIntConsumer<T> consumer) {
        Objects.requireNonNull(consumer);
        var counter = new Counter(0);
        iterable.forEach(it -> consumer.accept(it, counter.getAndIncr()));
    }

    @Override
    public <K> MapEntryChain<K, List<T>> groupBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        return IteratorMapEntryChainFactory.INSTANCE.from(() -> {
            var map = new HashMap<K, List<T>>();
            this.forEach(t -> map.computeIfAbsent(toKey.apply(t), k -> new ArrayList<>()).add(t));
            return map;
        });
    }

    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }

    @Override
    public <K> MapEntryChain<K, T> keyBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        return new IteratorMapEntryChain<>((IteratorChain<Map.Entry<K, T>>) this
                .map(it -> Map.entry(toKey.apply(it), it))
                .uniqueBy(Map.Entry::getKey));

    }

    @Override
    public <R> Chain<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return nest(iterable, itr -> new MapIterator<>(itr, mapper));
    }

    @Override
    public <R> Chain<R> map(ObjIntFunction<T, R> mapper) {
        Objects.requireNonNull(mapper);
        var counter = new Counter(0);
        return nest(iterable, itr -> new Map2Iterator<>(itr, mapper, counter));
    }

    @Override
    public <K extends Comparable<K>> Chain<T> orderBy(Function<T, K> toKey, Direction direction) {
        Objects.requireNonNull(toKey);
        Objects.requireNonNull(direction);
        return nest(iterable, itr -> new OrderByIterator<>(itr, toKey, direction));
    }

    @Override
    public <R> R reduce(R init, BiFunction<T, R, R> reducer) {
        Objects.requireNonNull(reducer);
        var result = init;
        for (var it : iterable) {
            result = reducer.apply(it, result);
        }
        return result;
    }

    @Override
    public Chain<T> reverse() {
        return nest(iterable, ReverseIterator::new);
    }

    @Override
    public Chain<T> skip(int n) {
        return nest(iterable, itr -> new SkipIterator<>(itr, n));
    }

    @Override
    public Chain<T> skipWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return nest(iterable, itr -> new SkipWhileIterator<>(itr, predicate));
    }

    @Override
    public Chain<T> take(int n) {
        return nest(iterable, itr -> new TakeIterator<>(itr, n));
    }

    @Override
    public Chain<T> takeWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return nest(iterable, itr -> new TakeWhileIterator<>(itr, predicate));
    }

    @Override
    public <R, T2> Chain<R> zip(Iterable<T2> zipped, BiFunction<T, T2, R> zipper) {
        Objects.requireNonNull(zipper);
        if (zipped == null) {
            return map(it -> zipper.apply(it, null));
        }

        return nest(iterable, itr -> new ZipIterator<>(itr, zipped.iterator(), zipper));
    }
}
