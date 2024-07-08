package com.xyzwps.lib.http;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public final class MediaTypeParameters {
    private final Map<String, String> map = new HashMap<>();

    public int size() {
        return map.size();
    }

    public Optional<String> get(String name) {
        name = Objects.requireNonNull(name, "MIME type parameter name cannot be null").toLowerCase();
        return Optional.ofNullable(this.map.get(name));
    }

    public boolean has(String name) {
        name = Objects.requireNonNull(name, "MIME type parameter name cannot be null").toLowerCase();
        return this.map.containsKey(name);
    }

    public void set(String name, String value) {
        name = Objects.requireNonNull(name, "MIME type parameter name cannot be null").toLowerCase();
        value = Objects.requireNonNull(value, "MIME type parameter value cannot be null");

        if (!MediaType.solelyContainsHTTPTokenCodePoints(name)) {
            throw new IllegalArgumentException("Invalid MIME type parameter name \"" + name +
                                               "\": only HTTP token code points are valid.");
        }
        if (!MediaType.soleyContainsHTTPQuotedStringTokenCodePoints(value)) {
            throw new IllegalArgumentException("Invalid MIME type parameter value \"" + value
                                               + "\":only HTTP quoted - string token code points are valid.");
        }
        this.map.put(name, value);
    }

    public void clear() {
        this.map.clear();
    }

    public void delete(String name) {
        name = Objects.requireNonNull(name, "Parameter name cannot be null").toLowerCase();
        this.map.remove(name);
    }

    public void forEach(BiConsumer<String, String> consumer) {
        this.map.forEach(consumer);
    }
}
