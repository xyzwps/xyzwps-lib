package com.xyzwps.lib.dollar.generator;

import com.xyzwps.lib.dollar.MapEntryChain;
import com.xyzwps.lib.dollar.MapEntryChainFactory;

import java.util.Map;

public enum GeneratorMapEntryChainFactory implements MapEntryChainFactory {

    INSTANCE;

    @Override
    public <K, V> MapEntryChain<K, V> empty() {
        return new GeneratorMapEntryChain<>(GeneratorChainFactory.INSTANCE.empty());
    }

    @Override
    public <K, V> MapEntryChain<K, V> from(Map<K, V> map) {
        return map == null ? empty() : new GeneratorMapEntryChain<>(GeneratorChainFactory.INSTANCE.from(map.entrySet()));
    }
}
