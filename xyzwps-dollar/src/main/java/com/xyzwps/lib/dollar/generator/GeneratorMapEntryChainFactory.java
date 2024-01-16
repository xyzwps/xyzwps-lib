package com.xyzwps.lib.dollar.generator;

import com.xyzwps.lib.dollar.MapEntryChain;
import com.xyzwps.lib.dollar.MapEntryChainFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

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

    public <K, V> MapEntryChain<K, V> from(Supplier<Map<K, V>> supplier) {
        Objects.requireNonNull(supplier);
        return new GeneratorMapEntryChain<>(new GeneratorChain<>(
                Generator.create(() -> {
                    var map = supplier.get();
                    return map == null ? Collections.emptySet() : map.entrySet();
                })
        ));
    }
}
