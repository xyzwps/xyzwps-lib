package com.xyzwps.lib.express.util;

import com.xyzwps.lib.bedrock.Args;

import java.util.*;
import java.util.function.BiConsumer;

// TODO: test
public class SimpleMultiValuesMap<K, V> implements MultiValuesMap<K, V> {

    private final Map<K, LinkedList<V>> map = new HashMap<>();

    public SimpleMultiValuesMap() {
    }

    @Override
    public void append(K name, V value) {
        Args.notNull(name, "Name cannot be null");
        Args.notNull(value, "Value cannot be null");

        map.computeIfAbsent(name, n -> new LinkedList<>()).add(value);
    }

    @Override
    public void delete(K name) {
        Args.notNull(name, "Name cannot be null");

        map.remove(name);
    }

    @Override
    public void forEach(BiConsumer<K, List<V>> callback) {
        map.forEach(Args.notNull(callback, "Callback function cannot be null"));
    }

    @Override
    public V get(K name) {
        Args.notNull(name, "Name cannot be null");

        var li = map.get(name);
        return li == null || li.isEmpty() ? null : li.getFirst();
    }

    @Override
    public List<V> getAll(K name) {
        Args.notNull(name, "Name cannot be null");

        var li = map.get(name);
        return li == null ? List.of() : li;
    }

    @Override
    public boolean has(K name) {
        Args.notNull(name, "Name cannot be null");

        return map.containsKey(name);
    }

    @Override
    public Set<K> names() {
        return map.keySet();
    }

    @Override
    public void set(K name, V value) {
        Args.notNull(name, "Name cannot be null");
        Args.notNull(value, "Value cannot be null");

        var li = new LinkedList<V>();
        li.add(value);
        map.put(name, li);
    }
}
