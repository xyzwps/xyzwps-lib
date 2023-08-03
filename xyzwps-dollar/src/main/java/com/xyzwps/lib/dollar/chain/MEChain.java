package com.xyzwps.lib.dollar.chain;

import com.xyzwps.lib.dollar.util.Pair;
import com.xyzwps.lib.dollar.util.Function3;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public interface MEChain<K, V> {

    Generator<Pair<K, V>> generator();

    default MEChain<K, V> filter(BiPredicate<K, V> predicateFn) {
        Objects.requireNonNull(predicateFn);
        return () -> this.generator().filter(p -> predicateFn.test(p.key(), p.value()));
    }

    default void forEach(BiConsumer<K, V> consumer) {
        Objects.requireNonNull(consumer);
        var gen = this.generator();
        while (gen.next() instanceof Result.Value<Pair<K, V>> p) {
            consumer.accept(p.value().key(), p.value().value());
        }
    }

    default Chain<K> keys() {
        return () -> this.generator().map(Pair::key);
    }

    default <K2> MEChain<K2, V> mapKeys(Function<K, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        return () -> this.generator()
                .map(p -> Pair.of(mapKeyFn.apply(p.key()), p.value()))
                .uniqueBy(Pair::key);
    }

    default <K2> MEChain<K2, V> mapKeys(BiFunction<K, V, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        return () -> this.generator()
                .map(p -> Pair.of(mapKeyFn.apply(p.key(), p.value()), p.value()))
                .uniqueBy(Pair::key);
    }

    default <V2> MEChain<K, V2> mapValues(Function<V, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return () -> this.generator().map(p -> Pair.of(p.key(), mapValueFn.apply(p.value())));
    }

    default <V2> MEChain<K, V2> mapValues(BiFunction<V, K, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return () -> this.generator().map(p -> Pair.of(p.key(), mapValueFn.apply(p.value(), p.key())));
    }

    default <R> R reduce(R initValue, Function3<R, K, V, R> callbackFn) {
        Objects.requireNonNull(callbackFn);
        R result = initValue;
        var gen = this.generator();
        while (gen.next() instanceof Result.Value<Pair<K, V>> v) {
            result = callbackFn.apply(result, v.value().key(), v.value().value());
        }
        return result;
    }

    default HashMap<K, V> value() {
        return this.reduce(new HashMap<>(), (m, k, v) -> {
            m.put(k, v);
            return m;
        });
    }

    default Chain<V> values() {
        return () -> this.generator().map(Pair::value);
    }

    // ------------ static ------------

    static <K, V> MEChain<K, V> empty() {
        return Generator::empty;
    }

    static <K, V> MEChain<K, V> from(Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            return empty();
        }
        return () -> Generator.fromIterator(map.entrySet().iterator())
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()));
    }
}
