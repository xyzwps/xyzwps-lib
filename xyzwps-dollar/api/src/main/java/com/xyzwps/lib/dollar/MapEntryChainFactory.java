package com.xyzwps.lib.dollar;

import java.util.Map;

/**
 * A factory for creating map entry chains.
 */
public interface MapEntryChainFactory {

    /**
     * Create an empty map entry chain.
     *
     * @param <K> the type of keys
     * @param <V> the type of values
     * @return the empty chain
     */
    <K, V> MapEntryChain<K, V> empty();

    /**
     * Create a map entry chain from a map.
     *
     * @param map the map
     * @param <K> the type of keys
     * @param <V> the type of values
     * @return the chain
     */
    <K, V> MapEntryChain<K, V> from(Map<K, V> map);
}
