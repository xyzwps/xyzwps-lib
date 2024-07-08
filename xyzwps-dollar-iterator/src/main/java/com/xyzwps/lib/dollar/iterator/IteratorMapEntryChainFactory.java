package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.MapEntryChain;
import com.xyzwps.lib.dollar.MapEntryChainFactory;
import com.xyzwps.lib.dollar.util.SharedUtils;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

public enum IteratorMapEntryChainFactory implements MapEntryChainFactory {
    INSTANCE;

    @Override
    public <K, V> MapEntryChain<K, V> empty() {
        return new IteratorMapEntryChain<>((Map<K, V>) null);
    }

    @Override
    public <K, V> MapEntryChain<K, V> from(Map<K, V> map) {
        return new IteratorMapEntryChain<>(map);
    }

    public <K, V> MapEntryChain<K, V> from(Supplier<Map<K, V>> supplier) {
        return new IteratorMapEntryChain<>(new IteratorChain<>(new LazyIterable<>(() -> SharedUtils.defaultTo(supplier.get(), Collections.<K, V>emptyMap()).entrySet())));
    }
}
