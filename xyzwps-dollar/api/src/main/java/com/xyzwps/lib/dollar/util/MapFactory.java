package com.xyzwps.lib.dollar.util;

import java.util.HashMap;

/**
 * A factory for creating maps.
 */
public interface MapFactory {


    /**
     * Create a {@link HashMap} with key-value pairs.
     *
     * @param <K> key type
     * @param <V> value type
     * @return new HashMap
     */
    default <K, V> HashMap<K, V> hashMap() {
        return new HashMap<>();
    }


    /**
     * Create a {@link HashMap} with key-value pairs.
     *
     * @param k1  the first key
     * @param v1  the first value
     * @param <K> key type
     * @param <V> value type
     * @return new HashMap
     */
    default <K, V> HashMap<K, V> hashMap(K k1, V v1) {
        var map = new HashMap<K, V>();
        map.put(k1, v1);
        return map;
    }


    /**
     * Create a {@link HashMap} with key-value pairs.
     *
     * @param k1  the first key
     * @param v1  the first value
     * @param k2  the second key
     * @param v2  the second value
     * @param <K> key type
     * @param <V> value type
     * @return new HashMap
     */
    default <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2) {
        var map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }


    /**
     * Create a {@link HashMap} with key-value pairs.
     *
     * @param k1  the first key
     * @param v1  the first value
     * @param k2  the second key
     * @param v2  the second value
     * @param k3  the third key
     * @param v3  the third value
     * @param <K> key type
     * @param <V> value type
     * @return new HashMap
     */
    default <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        var map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }


    /**
     * Create a {@link HashMap} with key-value pairs.
     *
     * @param k1  the first key
     * @param v1  the first value
     * @param k2  the second key
     * @param v2  the second value
     * @param k3  the third key
     * @param v3  the third value
     * @param k4  the fourth key
     * @param v4  the fourth value
     * @param <K> key type
     * @param <V> value type
     * @return new HashMap
     */
    default <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        var map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }


    /**
     * Create a {@link HashMap} with key-value pairs.
     *
     * @param k1  the first key
     * @param v1  the first value
     * @param k2  the second key
     * @param v2  the second value
     * @param k3  the third key
     * @param v3  the third value
     * @param k4  the fourth key
     * @param v4  the fourth value
     * @param k5  the fifth key
     * @param v5  the fifth value
     * @param <K> key type
     * @param <V> value type
     * @return new HashMap
     */
    default <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        var map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }


    /**
     * Create a {@link HashMap} with key-value pairs.
     *
     * @param k1  the first key
     * @param v1  the first value
     * @param k2  the second key
     * @param v2  the second value
     * @param k3  the third key
     * @param v3  the third value
     * @param k4  the fourth key
     * @param v4  the fourth value
     * @param k5  the fifth key
     * @param v5  the fifth value
     * @param k6  the sixth key
     * @param v6  the sixth value
     * @param <K> key type
     * @param <V> value type
     * @return new HashMap
     */
    default <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        var map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }


    /**
     * Create a {@link HashMap} with key-value pairs.
     *
     * @param k1  the first key
     * @param v1  the first value
     * @param k2  the second key
     * @param v2  the second value
     * @param k3  the third key
     * @param v3  the third value
     * @param k4  the fourth key
     * @param v4  the fourth value
     * @param k5  the fifth key
     * @param v5  the fifth value
     * @param k6  the sixth key
     * @param v6  the sixth value
     * @param k7  the seventh key
     * @param v7  the seventh value
     * @param <K> key type
     * @param <V> value type
     * @return new HashMap
     */
    default <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        var map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        return map;
    }


    /**
     * Create a {@link HashMap} with key-value pairs.
     *
     * @param k1  the first key
     * @param v1  the first value
     * @param k2  the second key
     * @param v2  the second value
     * @param k3  the third key
     * @param v3  the third value
     * @param k4  the fourth key
     * @param v4  the fourth value
     * @param k5  the fifth key
     * @param v5  the fifth value
     * @param k6  the sixth key
     * @param v6  the sixth value
     * @param k7  the seventh key
     * @param v7  the seventh value
     * @param k8  the eighth key
     * @param v8  the eighth value
     * @param <K> key type
     * @param <V> value type
     * @return new HashMap
     */
    default <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        var map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        return map;
    }


    /**
     * Create a {@link HashMap} with key-value pairs.
     *
     * @param k1  the first key
     * @param v1  the first value
     * @param k2  the second key
     * @param v2  the second value
     * @param k3  the third key
     * @param v3  the third value
     * @param k4  the fourth key
     * @param v4  the fourth value
     * @param k5  the fifth key
     * @param v5  the fifth value
     * @param k6  the sixth key
     * @param v6  the sixth value
     * @param k7  the seventh key
     * @param v7  the seventh value
     * @param k8  the eighth key
     * @param v8  the eighth value
     * @param k9  the ninth key
     * @param v9  the ninth value
     * @param <K> key type
     * @param <V> value type
     * @return new HashMap
     */
    default <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        var map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        return map;
    }


    /**
     * Create a {@link HashMap} with key-value pairs.
     *
     * @param k1  the first key
     * @param v1  the first value
     * @param k2  the second key
     * @param v2  the second value
     * @param k3  the third key
     * @param v3  the third value
     * @param k4  the fourth key
     * @param v4  the fourth value
     * @param k5  the fifth key
     * @param v5  the fifth value
     * @param k6  the sixth key
     * @param v6  the sixth value
     * @param k7  the seventh key
     * @param v7  the seventh value
     * @param k8  the eighth key
     * @param v8  the eighth value
     * @param k9  the ninth key
     * @param v9  the ninth value
     * @param k10 the tenth key
     * @param v10 the tenth value
     * @param <K> key type
     * @param <V> value type
     * @return new HashMap
     */
    default <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        var map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        return map;
    }

}
