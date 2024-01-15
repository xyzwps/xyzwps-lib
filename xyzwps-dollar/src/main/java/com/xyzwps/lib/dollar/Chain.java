package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.util.Counter;
import com.xyzwps.lib.dollar.util.ObjIntFunction;
import com.xyzwps.lib.dollar.util.ObjIntPredicate;

import java.util.*;
import java.util.function.*;

public interface Chain<T> {

    Chain<List<T>> chunk(int chunkSize);

    default Chain<T> compact() {
        return this.filter(it -> !Dollar.$.isFalsey(it));
    }

    Chain<T> concat(Iterable<T> iterable);

    Chain<T> filter(Predicate<T> predicate);

    Chain<T> filter(ObjIntPredicate<T> predicate);

    Optional<T> first();

    <R> Chain<R> flatMap(Function<T, Iterable<R>> flatter);

    void forEach(Consumer<T> consumer);

    void forEach(ObjIntConsumer<T> consumer);

    <K> MapEntryChain<K, List<T>> groupBy(Function<T, K> toKey);

    default Optional<T> head() {
        return first();
    }

    Iterator<T> iterator();

    default String join(String sep) {
        Objects.requireNonNull(sep);
        return this.reduce(new StringJoiner(sep), (it, joiner) -> {
            joiner.add(it == null ? null : it.toString());
            return joiner;
        }).toString();
    }

    <K> MapEntryChain<K, T> keyBy(Function<T, K> toKey);

    <R> Chain<R> map(Function<T, R> mapper);

    <R> Chain<R> map(ObjIntFunction<T, R> mapper);

    <K extends Comparable<K>> Chain<T> orderBy(Function<T, K> toKey, Direction direction);

    <R> R reduce(R init, BiFunction<T, R, R> reducer);

    Chain<T> reverse();

    default int size() {
        var counter = new Counter(0);
        this.forEach(it -> counter.incrAndGet());
        return counter.get();
    }

    Chain<T> skip(int n);

    Chain<T> skipWhile(Predicate<T> predicate);

    Chain<T> take(int n);

    Chain<T> takeWhile(Predicate<T> predicate);

    default ArrayList<T> toList() {
        return this.reduce(new ArrayList<>(), (it, list) -> {
            list.add(it);
            return list;
        });
    }

    default HashSet<T> toSet() {
        return this.reduce(new HashSet<>(), (it, list) -> {
            list.add(it);
            return list;
        });
    }

    default Chain<T> unique() {
        var set = new HashSet<T>();
        return this.filter(it -> {
            if (set.contains(it)) return false;
            set.add(it);
            return true;
        });
    }

    default <K> Chain<T> uniqueBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        var set = new HashSet<K>();
        return this.filter(it -> {
            var key = toKey.apply(it);
            if (set.contains(key)) return false;
            set.add(key);
            return true;
        });
    }

    // TODO: zip
}
