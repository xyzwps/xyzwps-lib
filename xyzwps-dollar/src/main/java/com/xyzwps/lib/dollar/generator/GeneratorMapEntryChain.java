package com.xyzwps.lib.dollar.generator;

import com.xyzwps.lib.dollar.Chain;
import com.xyzwps.lib.dollar.MapEntryChain;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class GeneratorMapEntryChain<K, V> implements MapEntryChain<K, V> {

    private final Chain<Map.Entry<K, V>> entryChain;

    public GeneratorMapEntryChain(Chain<Map.Entry<K, V>> entryChain) {
        this.entryChain = entryChain == null ? GeneratorChainFactory.INSTANCE.empty() : entryChain;
    }

    @Override
    public void forEach(BiConsumer<K, V> consumer) {
        Objects.requireNonNull(consumer);
        entryChain.forEach(entry -> consumer.accept(entry.getKey(), entry.getValue()));
    }

    @Override
    public MapEntryChain<K, V> filter(BiPredicate<K, V> predicate) {
        Objects.requireNonNull(predicate);
        return new GeneratorMapEntryChain<>(entryChain.filter(entry -> predicate.test(entry.getKey(), entry.getValue())));
    }

    @Override
    public Chain<K> keys() {
        return entryChain.map(Map.Entry::getKey);
    }

    @Override
    public <K2> MapEntryChain<K2, V> mapKeys(Function<K, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        return new GeneratorMapEntryChain<>(entryChain
                .map(entry -> Map.entry(mapKeyFn.apply(entry.getKey()), entry.getValue()))
                .uniqueBy(Map.Entry::getKey));
    }

    @Override
    public <K2> MapEntryChain<K2, V> mapKeys(BiFunction<K, V, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        return new GeneratorMapEntryChain<>(entryChain
                .map(entry -> Map.entry(mapKeyFn.apply(entry.getKey(), entry.getValue()), entry.getValue()))
                .uniqueBy(Map.Entry::getKey));
    }

    @Override
    public <V2> MapEntryChain<K, V2> mapValues(Function<V, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return new GeneratorMapEntryChain<>(entryChain
                .map(entry -> Map.entry(entry.getKey(), mapValueFn.apply(entry.getValue()))));
    }

    @Override
    public <V2> MapEntryChain<K, V2> mapValues(BiFunction<V, K, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return new GeneratorMapEntryChain<>(entryChain
                .map(entry -> Map.entry(entry.getKey(), mapValueFn.apply(entry.getValue(), entry.getKey()))));
    }

    @Override
    public Chain<V> values() {
        return entryChain.map(Map.Entry::getValue);
    }
}
