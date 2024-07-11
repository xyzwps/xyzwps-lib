package com.xyzwps.lib.dollar.seq;

import com.xyzwps.lib.dollar.MapEntryChain;
import com.xyzwps.lib.dollar.MapEntryChainFactory;
import com.xyzwps.lib.dollar.util.SharedUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A factory for creating map entry chains backed by a {@link Seq}.
 */
public enum SeqMapEntryChainFactory implements MapEntryChainFactory {
    /**
     * The singleton instance.
     */
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

    /**
     * Create a new chain of map entries from the specified map producer.
     *
     * @param mapProducer the producer of the map
     * @param <K>         the type of keys
     * @param <V>         the type of values
     * @return a new chain of map entries
     */
    public <K, V> MapEntryChain<K, V> from(Supplier<Map<K, V>> mapProducer) {
        Objects.requireNonNull(mapProducer);
        return new SeqMapEntryChain<>(Seq.create(() -> {
            Map<K, V> map = SharedUtils.defaultTo(mapProducer.get(), Collections.emptyMap());
            return map.entrySet();
        }));
    }
}
