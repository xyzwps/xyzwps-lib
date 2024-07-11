package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.util.ObjectHolder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * A sequence of map entries.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public interface MapEntrySeq<K, V> {

    /**
     * Iterate over the entries in the sequence.
     *
     * @param consumer the consumer function
     */
    void forEach(BiConsumer<K, V> consumer);

    /**
     * Filter the entries in the sequence.
     *
     * @param predicateFn the predicate function
     * @return the filtered sequence
     */
    default MapEntrySeq<K, V> filter(BiPredicate<K, V> predicateFn) {
        Objects.requireNonNull(predicateFn);
        return kvConsumer -> {
            Objects.requireNonNull(kvConsumer);
            this.forEach((k, v) -> {
                if (predicateFn.test(k, v)) {
                    kvConsumer.accept(k, v);
                }
            });
        };
    }

    /**
     * Get keys in the sequence.
     *
     * @return the sequence of keys
     */
    default Seq<K> keys() {
        return vConsumer -> this.forEach((k, v) -> vConsumer.accept(k));
    }

    /**
     * Map the keys in the sequence.
     *
     * @param mapKeyFn the mapping function
     * @param <K2>     the type of the new keys
     * @return the sequence of new entries
     */
    default <K2> MapEntrySeq<K2, V> mapKeys(Function<K, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        final HashSet<K2> dedupSet = new HashSet<>();
        return k2vConsumer -> {
            Objects.requireNonNull(k2vConsumer);
            this.forEach((k, v) -> {
                K2 k2 = mapKeyFn.apply(k);
                if (!dedupSet.contains(k2)) {
                    dedupSet.add(k2);
                    k2vConsumer.accept(k2, v);
                }
            });
        };
    }

    /**
     * Map the keys in the sequence.
     *
     * @param mapKeyFn the mapping function
     * @param <K2>     the type of the new keys
     * @return the sequence of new entries
     */
    default <K2> MapEntrySeq<K2, V> mapKeys(BiFunction<K, V, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        final HashSet<K2> dedupSet = new HashSet<>();
        return k2vConsumer -> {
            Objects.requireNonNull(k2vConsumer);
            this.forEach((k, v) -> {
                K2 k2 = mapKeyFn.apply(k, v);
                if (!dedupSet.contains(k2)) {
                    dedupSet.add(k2);
                    k2vConsumer.accept(k2, v);
                }
            });
        };
    }

    /**
     * Map the values in the sequence.
     *
     * @param mapValueFn the mapping function
     * @param <V2>       the type of the new values
     * @return the sequence of new entries
     */
    default <V2> MapEntrySeq<K, V2> mapValues(Function<V, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return kv2Consumer -> {
            Objects.requireNonNull(kv2Consumer);
            this.forEach((k, v) -> kv2Consumer.accept(k, mapValueFn.apply(v)));
        };
    }

    /**
     * Map the values in the sequence.
     *
     * @param mapValueFn the mapping function
     * @param <V2>       the type of the new values
     * @return the sequence of new entries
     */
    default <V2> MapEntrySeq<K, V2> mapValues(BiFunction<V, K, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return kv2Consumer -> {
            Objects.requireNonNull(kv2Consumer);
            this.forEach((k, v) -> kv2Consumer.accept(k, mapValueFn.apply(v, k)));
        };
    }

    /**
     * Reduce the entries in the sequence.
     *
     * @param initValue  the initial value
     * @param callbackFn the callback function
     * @param <R>        the type of the result
     * @return the reduced result
     */
    default <R> R reduce(R initValue, Function3<R, K, V, R> callbackFn) {
        Objects.requireNonNull(callbackFn);
        var rHolder = new ObjectHolder<>(initValue);
        this.forEach((k, v) -> rHolder.set(callbackFn.apply(rHolder.value(), k, v)));
        return rHolder.value();
    }

    /**
     * Get the value of the sequence.
     *
     * @return the value
     */
    default HashMap<K, V> value() {
        HashMap<K, V> result = new HashMap<>();
        this.forEach(result::put);
        return result;
    }

    default HashMap<K, V> toMap() {
        return value();
    }

    /**
     * Get values in the sequence.
     *
     * @return the sequence of values
     */
    default Seq<V> values() {
        return vConsumer -> this.forEach((k, v) -> vConsumer.accept(v));
    }

    // ------------ static ------------

    /**
     * Create an empty sequence.
     *
     * @param <K> the type of keys
     * @param <V> the type of values
     * @return the empty sequence
     */
    static <K, V> MapEntrySeq<K, V> empty() {
        return Objects::requireNonNull;
    }

    /**
     * Create a sequence from a map.
     *
     * @param map the map
     * @param <K> the type of keys
     * @param <V> the type of values
     * @return the sequence
     */
    static <K, V> MapEntrySeq<K, V> from(Map<K, V> map) {
        return map == null || map.isEmpty() ? empty() : (consumer -> {
            Objects.requireNonNull(consumer);
            map.forEach(consumer);
        });
    }
}
