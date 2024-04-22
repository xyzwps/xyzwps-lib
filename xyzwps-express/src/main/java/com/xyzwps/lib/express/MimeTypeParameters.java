package com.xyzwps.lib.express;

import com.xyzwps.lib.bedrock.Args;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public final class MimeTypeParameters {
    private final Map<String, String> map = new HashMap<>();

    public int size() {
        return map.size();
    }

    public Optional<String> get(String name) {
        name = Args.notNull(name, "MIME type parameter name cannot be null").toLowerCase();
        return Optional.ofNullable(this.map.get(name));
    }

    public boolean has(String name) {
        name = Args.notNull(name, "MIME type parameter name cannot be null").toLowerCase();
        return this.map.containsKey(name);
    }

    public void set(String name, String value) {
        name = Args.notNull(name, "MIME type parameter name cannot be null").toLowerCase();
        value = Args.notNull(value, "MIME type parameter value cannot be null");

        if (!MimeType.solelyContainsHTTPTokenCodePoints(name)) {
            throw new HttpException(String.format("Invalid MIME type parameter name \"%s\": only HTTP token code points are valid.", name), 400);
        }
        if (!MimeType.soleyContainsHTTPQuotedStringTokenCodePoints(value)) {
            throw new HttpException(String.format("Invalid MIME type parameter value \"%s\":only HTTP quoted - string token code points are valid.", value), 400);
        }
        this.map.put(name, value);
    }

    public void clear() {
        this.map.clear();
    }

    public void delete(String name) {
        name = Args.notNull(name, "Parameter name cannot be null").toLowerCase();
        this.map.remove(name);
    }

    public void forEach(BiConsumer<String, String> consumer) {
        this.map.forEach(consumer);
    }
}
