package com.xyzwps.lib.dollar.chain;

import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.util.*;

import static com.xyzwps.lib.dollar.Dollar.*;

import java.util.*;
import java.util.function.*;

public interface Chain<T> {

    Generator<T> generator();

    default Chain<List<T>> chunk(final int chunkSize) {
        return () -> this.generator().chunk(chunkSize);
    }

    default Chain<T> compact() {
        return this.filter(t -> !$.isFalsey(t));
    }

    default Chain<T> concat(Iterable<T> iterable) {
        if (iterable == null) {
            return this;
        }
        return () -> this.generator().concat(Generator.fromIterator(iterable.iterator()));
    }

    default Chain<T> filter(Predicate<T> predicate) {
        return () -> this.generator().filter(predicate);
    }

    default Chain<T> filter(ObjIntPredicate<T> predicate) {
        return () -> this.generator().filter(predicate);
    }

    default Optional<T> first() {
        return this.generator().next() instanceof Result.Value<T> v
                ? Optional.ofNullable(v.value())
                : Optional.empty();
    }

    default <R> Chain<R> flatMap(Function<T, Iterable<R>> flatMapFn) {
        return () -> this.generator().flatMap(flatMapFn);
    }

    default void forEach(Consumer<T> action) {
        Objects.requireNonNull(action);
        var gen = this.generator();
        while (gen.next() instanceof Result.Value<T> v) {
            action.accept(v.value());
        }
    }

    default void forEach(ObjIntConsumer<? super T> handler) {
        Objects.requireNonNull(handler);
        Counter counter = new Counter(0);
        this.forEach(t -> handler.accept(t, counter.getAndIncr()));
    }

    default <K> MEChain<K, List<T>> groupBy(Function<T, K> toKey) {
        var map = this.reduce(new HashMap<K, List<T>>(), (m, t) -> {
            m.computeIfAbsent(toKey.apply(t), (k) -> new ArrayList<>()).add(t);
            return m;
        });
        return new MEChain<>() {
            final Generator<K> keys = Generator.fromIterator(map.keySet().iterator());

            @Override
            public Generator<Pair<K, List<T>>> generator() {
                return keys.map(k -> Pair.of(k, map.get(k)));
            }
        };
    }

    default Optional<T> head() {
        return this.first();
    }

    default String join(String sep) {
        return this.reduce(new StringJoiner($.defaultTo(sep, "null")), (joiner, t) -> {
            joiner.add(t == null ? null : t.toString());
            return joiner;
        }).toString();
    }

    default <K> MEChain<K, T> keyBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        return () -> this.generator()
                .map(t -> Pair.of(toKey.apply(t), t))
                .uniqueBy(Pair::key);
    }

    default <R> Chain<R> map(Function<T, R> mapFn) {
        return () -> this.generator().map(mapFn);
    }

    default <R> Chain<R> map(ObjIntFunction<T, R> mapFn) {
        return () -> this.generator().map(mapFn);
    }

    default <S> Chain<S> operate(Function<Generator<T>, Generator<S>> operator) {
        Objects.requireNonNull(operator);
        return () -> operator.apply(this.generator());
    }

    default <K extends Comparable<K>> Chain<T> orderBy(Function<T, K> toKey, Direction direction) {
        return () -> this.generator().orderBy(toKey, direction);
    }

    default <R> R reduce(R initValue, BiFunction<R, T, R> reducer) {
        return this.generator().reduce(initValue, reducer);
    }

    default Chain<T> reverse() {
        return () -> this.generator().reverse();
    }

    default Chain<T> skip(int n) {
        return () -> this.generator().skip(n);
    }

    default Chain<T> skipWhile(Predicate<T> predicate) {
        return () -> this.generator().skipWhile(predicate);
    }

    default Chain<T> take(int n) {
        return () -> this.generator().take(n);
    }

    default Chain<T> takeWhile(Predicate<T> predicate) {
        return () -> this.generator().takeWhile(predicate);
    }

    default ArrayList<T> toList() {
        return this.reduce(new ArrayList<>(), (list, t) -> {
            list.add(t);
            return list;
        });
    }

    default HashSet<T> toSet() {
        return this.reduce(new HashSet<>(), (set, t) -> {
            set.add(t);
            return set;
        });
    }

    default Chain<T> unique() {
        return this.uniqueBy(Function.identity());
    }

    default <K> Chain<T> uniqueBy(Function<T, K> toKey) {
        return () -> this.generator().uniqueBy(toKey);
    }

    default ArrayList<T> value() {
        return this.toList();
    }

    default <R, T2> Chain<R> zip(Iterable<T2> iterable, BiFunction<T, T2, R> zipper) {
        Objects.requireNonNull(zipper);
        return iterable == null
                ? this.map(t -> zipper.apply(t, null))
                : () -> this.generator().zip(Generator.fromIterator(iterable.iterator()), zipper);
    }

    default <T2> Chain<Pair<T, T2>> zip(Iterable<T2> iterable) {
        return this.zip(iterable, Pair::of);
    }


    // ------------ static ------------

    static <E> Chain<E> empty() {
        return Generator::empty;
    }

    static <E> Chain<E> from(Iterable<E> iterable) {
        if (iterable == null) {
            return empty();
        }
        return () -> Generator.fromIterator(iterable.iterator());
    }

    @SafeVarargs
    static <E> Chain<E> just(E... elements) {
        if (elements.length == 0) {
            return empty();
        }
        return () -> Generator.fromArray(elements);
    }

    static Chain<Integer> range(int start, int end) {
        return () -> Generator.fromIterator(new Range(start, end).iterator());
    }


    static void main(String[] args) {
        Chain<Integer> chain = Chain.from($.listOf(1, 2, 3, 4));

        chain.map(i -> i * 3).filter(i -> i % 2 == 0).forEach(System.out::println);
    }
}
