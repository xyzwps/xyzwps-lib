package com.xyzwps.lib.express.server.undertow;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpHeaders;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

class UndertowHttpHeaders implements HttpHeaders {
    private final HeaderMap map;

    UndertowHttpHeaders(HeaderMap map) {
        this.map = Args.notNull(map, "HeaderMap cannot be null");
    }


    @Override
    public void append(String name, String value) {
        Args.notNull(name, "Header name cannot be null");
        Args.notNull(value, "Header value cannot be null");

        map.addLast(new HttpString(name), value);
    }

    @Override
    public void delete(String name) {
        Args.notNull(name, "Header name cannot be null");
        map.remove(name);
    }

    @Override
    public void forEach(BiConsumer<String, List<String>> callback) {
        Args.notNull(callback, "Callback cannot be null");

        map.forEach(it -> callback.accept(it.getHeaderName().toString(), it.stream().toList()));
    }

    @Override
    public String get(String name) {
        Args.notNull(name, "Header name cannot be null");
        return map.getFirst(name);
    }

    @Override
    public List<String> getAll(String name) {
        Args.notNull(name, "Header name cannot be null");
        return map.get(name).stream().toList();
    }

    @Override
    public boolean has(String name) {
        Args.notNull(name, "Header name cannot be null");
        return map.contains(name);
    }

    @Override
    public Set<String> names() {
        return map.getHeaderNames().stream().map(HttpString::toString).collect(Collectors.toSet());
    }

    @Override
    public void set(String name, String value) {
        Args.notNull(name, "Header name cannot be null");
        Args.notNull(value, "Header value cannot be null");

        map.put(new HttpString(name), value);
    }
}
