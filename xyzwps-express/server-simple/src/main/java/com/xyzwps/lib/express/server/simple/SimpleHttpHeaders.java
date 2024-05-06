package com.xyzwps.lib.express.server.simple;

import com.sun.net.httpserver.Headers;
import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpHeaders;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

class SimpleHttpHeaders implements HttpHeaders {
    private final Headers headers;

    SimpleHttpHeaders(Headers headers) {
        this.headers = Args.notNull(headers, "Headers cannot be null");
    }

    @Override
    public void append(String name, String value) {
        Args.notNull(name, "Header name cannot be null");
        Args.notNull(value, "Header value cannot be null");
        headers.add(name, value);
    }

    @Override
    public void delete(String name) {
        Args.notNull(name, "Header name cannot be null");
        headers.remove(name);
    }

    @Override
    public void forEach(BiConsumer<String, List<String>> callback) {
        Args.notNull(callback, "Callback cannot be null");
        headers.forEach(callback);
    }

    @Override
    public String get(String name) {
        Args.notNull(name, "Header name cannot be null");
        return headers.getFirst(name);
    }

    @Override
    public List<String> getAll(String name) {
        Args.notNull(name, "Header name cannot be null");
        return headers.get(name);
    }

    @Override
    public boolean has(String name) {
        Args.notNull(name, "Header name cannot be null");
        return headers.containsKey(name);
    }

    @Override
    public Set<String> names() {
        return headers.keySet();
    }

    @Override
    public void set(String name, String value) {
        Args.notNull(name, "Header name cannot be null");
        Args.notNull(value, "Header value cannot be null");
        headers.set(name, value);
    }
}
