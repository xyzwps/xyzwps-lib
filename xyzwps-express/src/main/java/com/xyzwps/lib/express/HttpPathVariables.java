package com.xyzwps.lib.express;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class HttpPathVariables {
    private final Map<String, String> data = new HashMap<>();

    public void add(String name, String value) {
        this.data.put(name, value);
    }

    public String get(String name) {
        return this.data.get(name);
    }

    public Set<String> names() {
        return data.keySet();
    }
}
