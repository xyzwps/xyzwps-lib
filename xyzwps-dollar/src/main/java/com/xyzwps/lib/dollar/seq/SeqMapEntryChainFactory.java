package com.xyzwps.lib.dollar.seq;

import com.xyzwps.lib.dollar.MapEntryChain;
import com.xyzwps.lib.dollar.MapEntryChainFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static com.xyzwps.lib.dollar.util.ObjectUtils.*;

public enum SeqMapEntryChainFactory implements MapEntryChainFactory {
    INSTANCE;

    @Override
    public <K, V> MapEntryChain<K, V> empty() {
        return new SeqMapEntryChain<>(null);
    }

    @Override
    public <K, V> MapEntryChain<K, V> from(Map<K, V> map) {
        if (map == null || map.isEmpty()) return empty();

        return new SeqMapEntryChain<>(Seq.from(map.entrySet()));
    }

    public <K, V> MapEntryChain<K, V> from(Supplier<Map<K, V>> supplier) {
        Objects.requireNonNull(supplier);
        return new SeqMapEntryChain<>(Seq.create(() -> {
            Map<K, V> map = defaultTo(supplier.get(), Collections.emptyMap());
            return map.entrySet();
        }));
    }
}
