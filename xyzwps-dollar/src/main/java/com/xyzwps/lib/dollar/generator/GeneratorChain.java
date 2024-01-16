package com.xyzwps.lib.dollar.generator;

import com.xyzwps.lib.dollar.Chain;
import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.MapEntryChain;
import com.xyzwps.lib.dollar.util.ObjIntFunction;
import com.xyzwps.lib.dollar.util.ObjIntPredicate;

import java.util.*;
import java.util.function.*;

public class GeneratorChain<T> implements Chain<T> {

    final Generator<T> generator;

    public GeneratorChain(Generator<T> generator) {
        this.generator = Objects.requireNonNull(generator);
    }

    @Override
    public Chain<List<T>> chunk(int chunkSize) {
        return new GeneratorChain<>(generator.chunk(chunkSize));
    }

    @Override
    public Chain<T> concat(Iterable<T> iterable) {
        if (iterable == null) return this;

        return new GeneratorChain<>(generator.concat(iterable));
    }

    @Override
    public Chain<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return new GeneratorChain<>(generator.filter(predicate));
    }

    @Override
    public Chain<T> filter(ObjIntPredicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return new GeneratorChain<>(generator.filter(predicate));
    }

    @Override
    public Optional<T> first() {
        return generator.first();
    }

    @Override
    public <R> Chain<R> flatMap(Function<T, Iterable<R>> flatter) {
        Objects.requireNonNull(flatter);
        return new GeneratorChain<>(generator.flatMap(flatter));
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        generator.forEach(consumer);
    }

    @Override
    public void forEach(ObjIntConsumer<T> consumer) {
        Objects.requireNonNull(consumer);
        generator.forEach(consumer);
    }

    @Override
    public <K> MapEntryChain<K, List<T>> groupBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        var map = new HashMap<K, List<T>>();
        this.forEach(t -> map.computeIfAbsent(toKey.apply(t), k -> new ArrayList<>()).add(t));
        return GeneratorMapEntryChainFactory.INSTANCE.from(map);
    }

    @Override
    public Iterator<T> iterator() {
        return generator.iterator();
    }

    @Override
    public <K> MapEntryChain<K, T> keyBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        return new GeneratorMapEntryChain<>(this
                .map(it -> Map.entry(toKey.apply(it), it))
                .uniqueBy(Map.Entry::getKey));
    }

    @Override
    public <R> Chain<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return new GeneratorChain<>(generator.map(mapper));
    }

    @Override
    public <R> Chain<R> map(ObjIntFunction<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return new GeneratorChain<>(generator.map(mapper));
    }

    @Override
    public <K extends Comparable<K>> Chain<T> orderBy(Function<T, K> toKey, Direction direction) {
        Objects.requireNonNull(toKey);
        Objects.requireNonNull(direction);
        return new GeneratorChain<>(generator.orderBy(toKey, direction));
    }

    @Override
    public <R> R reduce(R init, BiFunction<T, R, R> reducer) {
        Objects.requireNonNull(reducer);
        return generator.reduce(init, reducer);
    }

    @Override
    public Chain<T> reverse() {
        return new GeneratorChain<>(generator.reverse());
    }

    @Override
    public Chain<T> skip(int n) {
        return new GeneratorChain<>(generator.skip(n));
    }

    @Override
    public Chain<T> skipWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return new GeneratorChain<>(generator.skipWhile(predicate));
    }

    @Override
    public Chain<T> take(int n) {
        return new GeneratorChain<>(generator.take(n));
    }

    @Override
    public Chain<T> takeWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return new GeneratorChain<>(generator.takeWhile(predicate));
    }
}
