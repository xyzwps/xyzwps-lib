package com.xyzwps.lib.dollar.seq;

import com.xyzwps.lib.dollar.Chain;
import com.xyzwps.lib.dollar.MapEntryChain;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * A chain of map entries backed by a {@link Seq}.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class SeqMapEntryChain<K, V> implements MapEntryChain<K, V> {

    private final Seq<Map.Entry<K, V>> seq;

    /**
     * Create a new chain of map entries backed by the specified {@link Seq}.
     *
     * @param seq the sequence
     */
    public SeqMapEntryChain(Seq<Map.Entry<K, V>> seq) {
        this.seq = seq == null ? Seq.empty() : seq;
    }

    @Override
    public void forEach(BiConsumer<K, V> consumer) {
        Objects.requireNonNull(consumer);
        seq.forEach(entry -> consumer.accept(entry.getKey(), entry.getValue()));
    }

    @Override
    public MapEntryChain<K, V> filter(BiPredicate<K, V> predicateFn) {
        Objects.requireNonNull(predicateFn);
        return new SeqMapEntryChain<>(seq.filter(entry -> predicateFn.test(entry.getKey(), entry.getValue())));
    }

    @Override
    public Chain<K> keys() {
        return new SeqChain<>(seq.map(Map.Entry::getKey));
    }

    @Override
    public <K2> MapEntryChain<K2, V> mapKeys(Function<K, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        return new SeqMapEntryChain<>(seq
                .map(entry -> Map.entry(mapKeyFn.apply(entry.getKey()), entry.getValue()))
                .uniqueBy(Map.Entry::getKey));
    }

    @Override
    public <K2> MapEntryChain<K2, V> mapKeys(BiFunction<K, V, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        return new SeqMapEntryChain<>(seq
                .map(entry -> Map.entry(mapKeyFn.apply(entry.getKey(), entry.getValue()), entry.getValue()))
                .uniqueBy(Map.Entry::getKey));
    }

    @Override
    public <V2> MapEntryChain<K, V2> mapValues(Function<V, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return new SeqMapEntryChain<>(seq.map(entry -> Map.entry(entry.getKey(), mapValueFn.apply(entry.getValue()))));
    }

    @Override
    public <V2> MapEntryChain<K, V2> mapValues(BiFunction<V, K, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return new SeqMapEntryChain<>(seq.map(entry -> Map.entry(entry.getKey(), mapValueFn.apply(entry.getValue(), entry.getKey()))));
    }

    @Override
    public Chain<V> values() {
        return new SeqChain<>(seq.map(Map.Entry::getValue));
    }
}
