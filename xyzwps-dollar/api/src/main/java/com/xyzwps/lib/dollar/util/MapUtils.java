package com.xyzwps.lib.dollar.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class MapUtils {

    /**
     * Check if a {@link Map} is empty of not.
     *
     * @param map to be checked
     * @return true if map is null, or it has no any entries.
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Check if a {@link Map} is not empty.
     *
     * @param map to be checked
     * @return true if map {@link #isEmpty(Map)} is false
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * Create a new {@link Map} with new keys, which are mapped from the old keys.
     * If two new keys of old keys are the same, the last reached entry would be discarded.
     *
     * @param map   to be handled
     * @param mapFn mapping function
     * @param <K>   type of keys
     * @param <V>   type of values
     * @param <K2>  type of mapped keys
     * @return new {@link Map}
     */
    public static <K, V, K2> Map<K2, V> mapKeys(Map<K, V> map, Function<K, K2> mapFn) {
        Objects.requireNonNull(mapFn);

        if (isEmpty(map)) {
            return new HashMap<>();
        }

        Map<K2, V> result = new HashMap<>();
        map.forEach((k, v) -> {
            K2 k2 = mapFn.apply(k);
            if (!result.containsKey(k2)) {
                result.put(k2, v);
            }
        });
        return result;
    }

    /**
     * Create a new {@link Map} with new keys, which are mapped from the old keys.
     * If two new keys of different entries are equal, the last reached entry would be discarded.
     *
     * @param map   to be handled
     * @param mapFn mapping function
     * @param <K>   type of keys
     * @param <V>   type of values
     * @param <K2>  type of mapped keys
     * @return new {@link Map}
     */
    public static <K, V, K2> Map<K2, V> mapKeys(Map<K, V> map, BiFunction<K, V, K2> mapFn) {
        Objects.requireNonNull(mapFn);

        if (isEmpty(map)) {
            return new HashMap<>();
        }

        Map<K2, V> result = new HashMap<>();
        map.forEach((k, v) -> {
            K2 k2 = mapFn.apply(k, v);
            if (!result.containsKey(k2)) {
                result.put(k2, v);
            }
        });
        return result;
    }

    /**
     * Mapping {@link Map} values to another.
     *
     * @param map   {@link Map} to be handled.
     * @param mapFn mapping function
     * @param <K>   type of {@link Map} key
     * @param <V>   type of {@link Map} value
     * @param <V2>  type of mapping result
     * @return a new map
     */
    public static <K, V, V2> Map<K, V2> mapValues(Map<K, V> map, Function<V, V2> mapFn) {
        Objects.requireNonNull(mapFn);

        if (isEmpty(map)) {
            return new HashMap<>();
        }

        Map<K, V2> result = new HashMap<>();
        map.forEach((k, v) -> result.put(k, mapFn.apply(v)));
        return result;
    }

    /**
     * Mapping {@link Map} values to another.
     *
     * @param map   {@link Map} to be handled.
     * @param mapFn mapping function which accept key as the second argument
     * @param <K>   type of {@link Map} key
     * @param <V>   type of {@link Map} value
     * @param <V2>  type of mapping result
     * @return a new map
     */
    public static <K, V, V2> Map<K, V2> mapValues(Map<K, V> map, BiFunction<V, K, V2> mapFn) {
        Objects.requireNonNull(mapFn);

        if (isEmpty(map)) {
            return new HashMap<>();
        }

        Map<K, V2> result = new HashMap<>();
        map.forEach((k, v) -> result.put(k, mapFn.apply(v, k)));
        return result;
    }


    /**
     * Reducing {@link Iterable} to a value which is the accumulated result of running each element in
     * {@link Iterable} through reducer.
     *
     * @param map       to be handled
     * @param initValue the init value
     * @param reducer   reducer
     * @param <K>       type of map keys
     * @param <V>       type of map values
     * @param <R>       type of result
     * @return reducing result
     */
    public static <K, V, R> R reduce(Map<K, V> map, R initValue, Function3<R, K, V, R> reducer) {
        Objects.requireNonNull(reducer);

        if (map == null) {
            return initValue;
        }

        final Env1<R> env = new Env1<>();
        env.t = initValue;
        map.forEach((k, v) -> env.t = reducer.apply(env.t, k, v));
        return env.t;
    }

    private static class Env1<T> {
        T t;
    }

    /**
     * Count the entries of a <code>map</code>.
     * Return 0 if <code>map</code> is <code>null</code>.
     *
     * @param map which to handle
     * @param <K> type of keys
     * @param <V> type of values
     * @return count of entries in map
     */
    public static <K, V> int size(Map<K, V> map) {
        return map == null ? 0 : map.size();
    }


    private MapUtils() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
