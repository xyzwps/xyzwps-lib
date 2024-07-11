package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.util.Function3;
import com.xyzwps.lib.dollar.util.ObjectHolder;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * A chain of map entries.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public interface MapEntryChain<K, V> {

    /**
     * Iterate over the entries in the chain.
     *
     * @param consumer the consumer function
     */
    void forEach(BiConsumer<K, V> consumer);

    /**
     * Filter the entries in the chain.
     *
     * @param predicateFn the predicate function
     * @return the filtered chain
     */
    MapEntryChain<K, V> filter(BiPredicate<K, V> predicateFn);

    /**
     * Get keys in the chain.
     *
     * @return the chain of keys
     */
    Chain<K> keys();

    /**
     * Map the keys in the chain.
     *
     * @param mapKeyFn the mapping function
     * @param <K2>     the type of the new keys
     * @return the chain of new entries
     */
    <K2> MapEntryChain<K2, V> mapKeys(Function<K, K2> mapKeyFn);

    /**
     * Map the keys in the chain.
     *
     * @param mapKeyFn the mapping function
     * @param <K2>     the type of the new keys
     * @return the chain of new entries
     */
    <K2> MapEntryChain<K2, V> mapKeys(BiFunction<K, V, K2> mapKeyFn);

    /**
     * Map the values in the chain.
     *
     * @param mapValueFn the mapping function
     * @param <V2>       the type of the new values
     * @return the chain of new entries
     */
    <V2> MapEntryChain<K, V2> mapValues(Function<V, V2> mapValueFn);

    /**
     * Map the values in the chain.
     *
     * @param mapValueFn the mapping function
     * @param <V2>       the type of the new values
     * @return the chain of new entries
     */
    <V2> MapEntryChain<K, V2> mapValues(BiFunction<V, K, V2> mapValueFn);

    /**
     * Reduce the entries in the chain.
     *
     * @param initValue  the initial value
     * @param callbackFn the callback function
     * @param <R>        the type of the result
     * @return the reduced result
     */
    default <R> R reduce(R initValue, Function3<K, V, R, R> callbackFn) {
        Objects.requireNonNull(callbackFn);
        var result = new ObjectHolder<>(initValue);
        this.forEach((k, v) -> result.set(callbackFn.apply(k, v, result.value())));
        return result.value();
    }

    /**
     * Convert the chain to a map.
     *
     * @return the map
     */
    default HashMap<K, V> toMap() {
        var result = new HashMap<K, V>();
        this.forEach(result::put);
        return result;
    }

    /**
     * Get values in the chain.
     *
     * @return the chain of values
     */
    Chain<V> values();
}
