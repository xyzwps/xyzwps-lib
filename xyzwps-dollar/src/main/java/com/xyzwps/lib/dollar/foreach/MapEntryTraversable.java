package com.xyzwps.lib.dollar.foreach;

import com.xyzwps.lib.dollar.util.Function3;
import com.xyzwps.lib.dollar.util.Functions;
import com.xyzwps.lib.dollar.util.ObjectHolder;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;


public interface MapEntryTraversable<K, V> {

    void forEach(BiConsumer<K, V> consumer);

    default MapEntryTraversable<K, V> filter(BiPredicate<K, V> predicateFn) {
        Objects.requireNonNull(predicateFn);
        return kvConsumer -> this.forEach((k, v) -> {
            if (predicateFn.test(k, v)) {
                kvConsumer.accept(k, v);
            }
        });
    }

    default Traversable<K> keys() {
        return vConsumer -> this.forEach((k, v) -> vConsumer.accept(k));
    }

    default <K2> MapEntryTraversable<K2, V> mapKeys(Function<K, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        final HashSet<K2> dedupSet = new HashSet<>();
        return k2vConsumer -> this.forEach((k, v) -> {
            K2 k2 = mapKeyFn.apply(k);
            if (!dedupSet.contains(k2)) {
                dedupSet.add(k2);
                k2vConsumer.accept(k2, v);
            }
        });
    }

    default <K2> MapEntryTraversable<K2, V> mapKeys(BiFunction<K, V, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        final HashSet<K2> dedupSet = new HashSet<>();
        return k2vConsumer -> this.forEach((k, v) -> {
            K2 k2 = mapKeyFn.apply(k, v);
            if (!dedupSet.contains(k2)) {
                dedupSet.add(k2);
                k2vConsumer.accept(k2, v);
            }
        });
    }

    default <V2> MapEntryTraversable<K, V2> mapValues(Function<V, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return kv2Consumer -> this.forEach((k, v) -> kv2Consumer.accept(k, mapValueFn.apply(v)));
    }

    default <V2> MapEntryTraversable<K, V2> mapValues(BiFunction<V, K, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return kv2Consumer -> this.forEach((k, v) -> kv2Consumer.accept(k, mapValueFn.apply(v, k)));
    }

    default <R> R reduce(R initValue, Function3<R, K, V, R> callbackFn) {
        Objects.requireNonNull(callbackFn);
        var rHolder = new ObjectHolder<>(initValue);
        this.forEach((k, v) -> rHolder.set(callbackFn.apply(rHolder.value(), k, v)));
        return rHolder.value();
    }

    default HashMap<K, V> value() {
        HashMap<K, V> result = new HashMap<>();
        this.forEach(result::put);
        return result;
    }

    default Traversable<V> values() {
        return vConsumer -> this.forEach((k, v) -> vConsumer.accept(v));
    }

    // ------------ static ------------

    static <K, V> MapEntryTraversable<K, V> empty() {
        return Functions::consumeNothing;
    }

    static <K, V> MapEntryTraversable<K, V> from(Map<K, V> map) {
        return map == null || map.isEmpty() ? empty() : map::forEach;
    }
}
