package com.xyzwps.lib.dollar;

import java.util.Map;

public interface MapEntryChainFactory {

    <K, V> MapEntryChain<K, V> empty();

    <K, V> MapEntryChain<K, V> from(Map<K, V> map);
}
