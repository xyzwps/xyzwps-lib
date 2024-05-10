package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.Chain;
import com.xyzwps.lib.dollar.MapEntryChain;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static java.util.Map.Entry;

public class IteratorMapEntryChain<K, V> implements MapEntryChain<K, V> {

    private final IteratorChain<Entry<K, V>> entries;

    public IteratorMapEntryChain(Map<K, V> map) {
        this.entries = map == null ? new IteratorChain<>(null) : new IteratorChain<>(map.entrySet());
    }

    IteratorMapEntryChain(IteratorChain<Entry<K, V>> entries) {
        this.entries = entries == null ? new IteratorChain<>(null) : entries;
    }

    @Override
    public void forEach(BiConsumer<K, V> consumer) {
        Objects.requireNonNull(consumer);
        entries.forEach(entry -> consumer.accept(entry.getKey(), entry.getValue()));
    }

    @Override
    public MapEntryChain<K, V> filter(BiPredicate<K, V> predicateFn) {
        Objects.requireNonNull(predicateFn);
        return new IteratorMapEntryChain<>((IteratorChain<Entry<K, V>>) entries
                .filter(entry -> predicateFn.test(entry.getKey(), entry.getValue()))
                .uniqueBy(Entry::getKey));
    }

    @Override
    public Chain<K> keys() {
        return entries.map(Entry::getKey);
    }

    @Override
    public <K2> MapEntryChain<K2, V> mapKeys(Function<K, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        return new IteratorMapEntryChain<>((IteratorChain<Entry<K2, V>>) entries
                .map(entry -> Map.entry(mapKeyFn.apply(entry.getKey()), entry.getValue()))
                .uniqueBy(Entry::getKey));
    }

    @Override
    public <K2> MapEntryChain<K2, V> mapKeys(BiFunction<K, V, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        return new IteratorMapEntryChain<>((IteratorChain<Entry<K2, V>>) entries
                .map(entry -> Map.entry(mapKeyFn.apply(entry.getKey(), entry.getValue()), entry.getValue()))
                .uniqueBy(Entry::getKey));
    }

    @Override
    public <V2> MapEntryChain<K, V2> mapValues(Function<V, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return new IteratorMapEntryChain<>((IteratorChain<Entry<K, V2>>) entries
                .map(entry -> Map.entry(entry.getKey(), mapValueFn.apply(entry.getValue()))));
    }

    @Override
    public <V2> MapEntryChain<K, V2> mapValues(BiFunction<V, K, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return new IteratorMapEntryChain<>((IteratorChain<Entry<K, V2>>) entries
                .map(entry -> Map.entry(entry.getKey(), mapValueFn.apply(entry.getValue(), entry.getKey()))));
    }

    @Override
    public Chain<V> values() {
        return entries.map(Entry::getValue);
    }
}
