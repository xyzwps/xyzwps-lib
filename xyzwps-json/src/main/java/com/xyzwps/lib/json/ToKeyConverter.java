package com.xyzwps.lib.json;

public interface ToKeyConverter<K> {

    /**
     * @param key never be null
     */
    String convert(K key);
}
