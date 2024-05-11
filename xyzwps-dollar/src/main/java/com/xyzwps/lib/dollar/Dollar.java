package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.seq.SeqChainFactory;
import com.xyzwps.lib.dollar.seq.SeqMapEntryChainFactory;
import com.xyzwps.lib.dollar.util.*;

import java.util.*;
import java.util.function.*;


/**
 * Where to start.
 * <p>
 * TODO: optimize for RandomAccess
 * TODO: 写中文文档
 * TODO: add examples
 * TODO: all apis should be null tolerant
 */
public final class Dollar {

    private static final ChainFactory cf = SeqChainFactory.INSTANCE;
    private static final MapEntryChainFactory mf = SeqMapEntryChainFactory.INSTANCE;


    /**
     * Create a stage chain from a {@link List}.
     *
     * @param list source list. Null is acceptable.
     * @param <T>  list element type
     * @return a list stage
     */
    public static <T> Chain<T> $(Iterable<T> list) {
        return cf.from(list);
    }


    /**
     * Create a stage chain from a map.
     *
     * @param map source map. Null is acceptable.
     * @param <K> map key type
     * @param <V> map value type
     * @return a map stage
     */
    public static <K, V> MapEntryChain<K, V> $(Map<K, V> map) {
        return mf.from(map);
    }


    public static final class $ {


        public static <T> T defaultTo(T value, T defaultValue) {
            return ObjectUtils.defaultTo(value, defaultValue);
        }

        public static boolean isFalsey(Object value) {
            return ObjectUtils.isFalsey(value);
        }

        public static <T> List<List<T>> chunk(List<T> list, int size) {
            return CollectionUtils.chunk(list, size);
        }

        public static <T> List<T> compact(List<T> list) {
            return CollectionUtils.compact(list);
        }

        @SafeVarargs
        public static <T> List<T> concat(List<T>... lists) {
            return CollectionUtils.concat(lists);
        }

        public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
            return CollectionUtils.filter(list, predicate);
        }

        public static <T> List<T> filter(List<T> list, BiPredicate<T, Integer> predicate) {
            return CollectionUtils.filter(list, predicate);
        }

        @SafeVarargs
        public static <T> ArrayList<T> arrayList(T... args) {
            return ListFactory.arrayList(args);
        }

        public static <T> List<T> listFrom(Iterator<T> itr) {
            return ListFactory.arrayListFrom(itr);
        }

        public static <T, R> List<R> map(Iterable<T> iterable, Function<T, R> mapFn) {
            return CollectionUtils.map(iterable, mapFn);
        }

        public static <T> Optional<T> last(List<T> list) {
            return CollectionUtils.last(list);
        }

        public static int length(String str) {
            return StringUtils.length(str);
        }

        public static <T> Optional<T> tail(List<T> list) {
            return CollectionUtils.last(list);
        }

        public static <T, R> List<R> map(Iterable<T> iterable, ObjIntFunction<T, R> mapFn) {
            return CollectionUtils.map(iterable, mapFn);
        }


        public static <K, V> HashMap<K, V> hashMap() {
            return MapFactory.hashMap();
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1) {
            return MapFactory.hashMap(k1, v1);
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2) {
            return MapFactory.hashMap(k1, v1, k2, v2);
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3) {
            return MapFactory.hashMap(k1, v1, k2, v2, k3, v3);
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
            return MapFactory.hashMap(k1, v1, k2, v2, k3, v3, k4, v4);
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
            return MapFactory.hashMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
            return MapFactory.hashMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
            return MapFactory.hashMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
            return MapFactory.hashMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
            return MapFactory.hashMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
            return MapFactory.hashMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
        }

        public static boolean isEmpty(String string) {
            return StringUtils.isEmpty(string);
        }

        /**
         * Check if a {@link Map} is empty of not.
         *
         * @param map to be checked
         * @return true if map is null, or it has no any entries.
         */
        public static boolean isEmpty(Map<?, ?> map) {
            return map == null || map.isEmpty();
        }

        public static <T> boolean isNotEmpty(Collection<T> collection) {
            return CollectionUtils.isNotEmpty(collection);
        }

        public static boolean isEmpty(Collection<?> c) {
            return CollectionUtils.isEmpty(c);
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

        public static boolean isNotEmpty(String string) {
            return StringUtils.isNotEmpty(string);
        }

        public static String pad(String string, int length, String chars) {
            return StringUtils.pad(string, length, chars);
        }

        public static String padEnd(String string, int length, String chars) {
            return StringUtils.padEnd(string, length, chars);
        }

        public static String padStart(String string, int length, String chars) {
            return StringUtils.padStart(string, length, chars);
        }

        /**
         * Create an empty list stage.
         *
         * @param <T> element type
         * @return list stage
         */
        public static <T> Chain<T> empty() {
            return cf.empty();
        }


        /**
         * Create a stage from elements.
         *
         * @param args elements to be handled
         * @param <T>  type of elements
         * @return list stage
         */
        @SafeVarargs
        public static <T> Chain<T> just(T... args) {
            return cf.just(args);
        }


        /**
         * Handle a range.
         *
         * @param start range start - included
         * @param end   range end - excluded
         * @return list stage
         */
        public static Chain<Integer> range(int start, int end) {
            return cf.range(start, end);
        }

        public static <T> Optional<T> first(Iterable<T> iterable) {
            return CollectionUtils.first(iterable);
        }

        public static <T> Optional<T> head(Iterable<T> iterable) {
            return CollectionUtils.first(iterable);
        }

        public static <T, R> List<R> flatMap(Iterable<T> iterable, Function<T, Iterable<R>> flatMapFn) {
            return CollectionUtils.flatMap(iterable, flatMapFn);
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


        public static <T, K> Map<K, T> keyBy(Iterable<T> iterable, Function<T, K> toKey) {
            return CollectionUtils.keyBy(iterable, toKey);
        }


        public static <T, K> Map<K, List<T>> groupBy(Iterable<T> iterable, Function<T, K> toKey) {
            return CollectionUtils.groupBy(iterable, toKey);
        }

        public static <T, K extends Comparable<K>> List<T> orderBy(Iterable<T> iterable, Function<T, K> toKey, Direction direction) {
            return CollectionUtils.orderBy(iterable, toKey, direction);
        }

        public static <T, R> R reduce(Iterable<T> iterable, R initValue, BiFunction<R, T, R> reducer) {
            return CollectionUtils.reduce(iterable, initValue, reducer);
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

        public static <T> List<T> reverse(Iterable<T> iterable) {
            return CollectionUtils.reverse(iterable);
        }

        public static <E> int size(Collection<E> collection) {
            return CollectionUtils.size(collection);
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

        public static <T> List<T> take(Iterable<T> iterable, int n) {
            return CollectionUtils.take(iterable, n);
        }

        public static final String EMPTY_STRING = "";

        public static String take(final String str, final int n) {
            return StringUtils.take(str, n);
        }

        public static String takeRight(final String str, final int n) {
            return StringUtils.takeRight(str, n);
        }

        public static <T> List<T> takeWhile(Iterable<T> iterable, Predicate<T> predicate) {
            return CollectionUtils.takeWhile(iterable, predicate);
        }

        public static <T> Set<T> toSet(Iterable<T> iterable) {
            return CollectionUtils.toSet(iterable);
        }

        public static <T> void forEach(Iterable<T> iterable, Consumer<T> handler) {
            CollectionUtils.forEach(iterable, handler);
        }

        public static <T> void forEach(Iterable<T> iterable, ObjIntConsumer<T> handler) {
            CollectionUtils.forEach(iterable, handler);
        }

        public static <T> List<T> unique(Iterable<T> iterable) {
            return CollectionUtils.unique(iterable);
        }

        public static <T, K> List<T> uniqueBy(Iterable<T> iterable, Function<T, K> toKey) {
            return CollectionUtils.uniqueBy(iterable, toKey);
        }

        public static <T, K> List<T> uniqueBy(Iterable<T> iterable, ObjIntFunction<T, K> toKey) {
            return CollectionUtils.uniqueBy(iterable, toKey);
        }

        public static <T, R> List<Pair<T, R>> zip(Iterable<T> left, Iterable<R> right) {
            return CollectionUtils.zip(left, right);
        }

        public static <T, R, S> List<S> zip(Iterable<T> left, Iterable<R> right, BiFunction<T, R, S> combineFn) {
            return CollectionUtils.zip(left, right, combineFn);
        }
    }


    private Dollar() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
