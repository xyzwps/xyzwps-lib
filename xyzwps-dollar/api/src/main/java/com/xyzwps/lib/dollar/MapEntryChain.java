package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.util.Function3;
import com.xyzwps.lib.dollar.util.ObjectHolder;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public interface MapEntryChain<K, V> {

    void forEach(BiConsumer<K, V> consumer);

    MapEntryChain<K, V> filter(BiPredicate<K, V> predicateFn);

    Chain<K> keys();

    <K2> MapEntryChain<K2, V> mapKeys(Function<K, K2> mapKeyFn);

    <K2> MapEntryChain<K2, V> mapKeys(BiFunction<K, V, K2> mapKeyFn);

    <V2> MapEntryChain<K, V2> mapValues(Function<V, V2> mapValueFn);

    <V2> MapEntryChain<K, V2> mapValues(BiFunction<V, K, V2> mapValueFn);

    default <R> R reduce(R initValue, Function3<K, V, R, R> callbackFn) {
        Objects.requireNonNull(callbackFn);
        var result = new ObjectHolder<>(initValue);
        this.forEach((k, v) -> result.set(callbackFn.apply(k, v, result.value())));
        return result.value();
    }

    default HashMap<K, V> toMap() {
        var result = new HashMap<K, V>();
        this.forEach(result::put);
        return result;
    }



    Chain<V> values();
}
