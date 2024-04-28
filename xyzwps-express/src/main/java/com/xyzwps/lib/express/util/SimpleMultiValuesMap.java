package com.xyzwps.lib.express.util;

import com.xyzwps.lib.bedrock.Args;

import java.util.*;
import java.util.function.BiConsumer;

// TODO: test
public class SimpleMultiValuesMap implements MultiValuesMap {

    private final Map<String, LinkedList<String>> map = new TreeMap<>();
    private final boolean casesOfNameIgored;

    public SimpleMultiValuesMap(boolean casesOfNameIgored) {
        this.casesOfNameIgored = casesOfNameIgored;
    }

    private String iname(String name) {
        return casesOfNameIgored ? name.toLowerCase() : name;
    }

    @Override
    public void append(String name, String value) {
        Args.notNull(name, "Name cannot be null");
        Args.notNull(value, "Value cannot be null");

        name = iname(name);

        map.computeIfAbsent(name, n -> new LinkedList<>()).add(value);
    }

    @Override
    public void delete(String name) {
        Args.notNull(name, "Name cannot be null");

        name = iname(name);

        map.remove(name);
    }

    @Override
    public void forEach(BiConsumer<String, List<String>> callback) {
        map.forEach(Args.notNull(callback, "Callback function cannot be null"));
    }

    @Override
    public String get(String name) {
        Args.notNull(name, "Name cannot be null");

        name = iname(name);

        var li = map.get(name);
        return li == null || li.isEmpty() ? null : li.getFirst();
    }

    @Override
    public List<String> getAll(String name) {
        Args.notNull(name, "Name cannot be null");

        name = iname(name);

        var li = map.get(name);
        return li == null ? List.of() : li;
    }

    @Override
    public boolean has(String name) {
        Args.notNull(name, "Name cannot be null");

        name = iname(name);

        return map.containsKey(name);
    }

    @Override
    public Set<String> keys() {
        return map.keySet();
    }

    @Override
    public void set(String name, String value) {
        Args.notNull(name, "Name cannot be null");
        Args.notNull(value, "Value cannot be null");

        name = iname(name);

        var li = new LinkedList<String>();
        li.add(value);
        map.put(name, li);
    }
}
