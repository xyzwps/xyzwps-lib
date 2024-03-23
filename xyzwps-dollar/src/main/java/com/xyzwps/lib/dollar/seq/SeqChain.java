package com.xyzwps.lib.dollar.seq;

import com.xyzwps.lib.dollar.Chain;
import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.MapEntryChain;
import com.xyzwps.lib.dollar.iterator.LazyIterator;
import com.xyzwps.lib.dollar.util.ObjIntFunction;
import com.xyzwps.lib.dollar.util.ObjIntPredicate;

import java.util.*;
import java.util.function.*;

public class SeqChain<T> implements Chain<T> {

    private final Seq<T> seq;

    public SeqChain(Seq<T> seq) {
        this.seq = seq == null ? Seq.empty() : seq;
    }

    @Override
    public Chain<List<T>> chunk(int chunkSize) {
        return new SeqChain<>(seq.chunk(chunkSize));
    }

    @Override
    public Chain<T> concat(Iterable<T> iterable) {
        return iterable == null ? this : new SeqChain<>(seq.concat(iterable));
    }

    @Override
    public Chain<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return new SeqChain<>(seq.filter(predicate));
    }

    @Override
    public Chain<T> filter(ObjIntPredicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return new SeqChain<>(seq.filter(predicate));
    }

    @Override
    public Optional<T> first() {
        return seq.first();
    }

    @Override
    public <R> Chain<R> flatMap(Function<T, Iterable<R>> flatter) {
        Objects.requireNonNull(flatter);
        return new SeqChain<>(seq.flatMap(it -> Seq.from(flatter.apply(it))));
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        seq.forEach(consumer);
    }

    @Override
    public void forEach(ObjIntConsumer<T> consumer) {
        Objects.requireNonNull(consumer);
        seq.forEach(consumer);
    }

    @Override
    public <K> MapEntryChain<K, List<T>> groupBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        return SeqMapEntryChainFactory.INSTANCE.from(() -> {
            Map<K, List<T>> map = new HashMap<>();
            this.forEach(t -> map.computeIfAbsent(toKey.apply(t), k -> new ArrayList<>()).add(t));
            return map;
        });
    }

    @Override
    public Iterator<T> iterator() {
        return new LazyIterator<>(() -> seq.toList().iterator());
    }

    @Override
    public <K> MapEntryChain<K, T> keyBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        return new SeqMapEntryChain<>(seq
                .map(it -> Map.entry(toKey.apply(it), it))
                .uniqueBy(Map.Entry::getKey));
    }

    @Override
    public <R> Chain<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return new SeqChain<>(seq.map(mapper));
    }

    @Override
    public <R> Chain<R> map(ObjIntFunction<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return new SeqChain<>(seq.map(mapper));
    }

    @Override
    public <K extends Comparable<K>> Chain<T> orderBy(Function<T, K> toKey, Direction direction) {
        Objects.requireNonNull(toKey);
        Objects.requireNonNull(direction);
        return new SeqChain<>(seq.orderBy(toKey, direction));
    }

    @Override
    public <R> R reduce(R init, BiFunction<T, R, R> reducer) {
        Objects.requireNonNull(reducer);
        return seq.reduce(init, reducer);
    }

    @Override
    public Chain<T> reverse() {
        return new SeqChain<>(seq.reverse());
    }

    @Override
    public Chain<T> skip(int n) {
        return new SeqChain<>(seq.skip(n));
    }

    @Override
    public Chain<T> skipWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return new SeqChain<>(seq.skipWhile(predicate));
    }

    @Override
    public Chain<T> take(int n) {
        return new SeqChain<>(seq.take(n));
    }

    @Override
    public Chain<T> takeWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return new SeqChain<>(seq.takeWhile(predicate));
    }

    @Override
    public <R, T2> Chain<R> zip(Iterable<T2> iterable, BiFunction<T, T2, R> zipper) {
        Objects.requireNonNull(zipper);
        if (iterable == null) {
            return this.map(it -> zipper.apply(it, null));
        }

        return new SeqChain<>(consumer -> {
            var itr2 = iterable.iterator();
            seq.forEach(it -> consumer.accept(zipper.apply(it, itr2.hasNext() ? itr2.next() : null)));
            while (itr2.hasNext()) {
                consumer.accept(zipper.apply(null, itr2.next()));
            }
        });

    }
}
