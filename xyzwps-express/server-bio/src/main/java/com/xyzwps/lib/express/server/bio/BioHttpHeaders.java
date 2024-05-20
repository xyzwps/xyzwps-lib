package com.xyzwps.lib.express.server.bio;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.util.MultiValuesMap;
import com.xyzwps.lib.express.util.SimpleMultiValuesMap;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import static com.xyzwps.lib.dollar.Dollar.*;

// TODO: test
// TODO: 不要 implement Map，同时实现 JSONSerializer
final class BioHttpHeaders implements HttpHeaders {

    private final MultiValuesMap<HttpHeaderName, String> map = new SimpleMultiValuesMap<>();

    @Override
    public void append(String name, String value) {
        Args.notNull(name, "Name cannot be null");
        Args.notNull(value, "Value cannot be null");

        this.map.append(new HttpHeaderName(name), value);
    }

    @Override
    public void delete(String name) {
        Args.notNull(name, "Name cannot be null");

        this.map.delete(new HttpHeaderName(name));
    }

    @Override
    public void forEach(BiConsumer<String, List<String>> callback) {
        Args.notNull(callback, "Callback function cannot be null");

        this.map.forEach((name, value) -> callback.accept(name.name, value));
    }

    @Override
    public String get(String name) {
        Args.notNull(name, "Name cannot be null");

        return this.map.get(new HttpHeaderName(name));
    }

    @Override
    public List<String> getAll(String name) {
        Args.notNull(name, "Name cannot be null");

        return this.map.getAll(new HttpHeaderName(name));
    }

    @Override
    public boolean has(String name) {
        Args.notNull(name, "Name cannot be null");

        return this.map.has(new HttpHeaderName(name));
    }

    @Override
    public Set<String> names() {
        return $(this.map.names()).map(it -> it.name).toSet();
    }

    @Override
    public void set(String name, String value) {
        Args.notNull(name, "Name cannot be null");
        Args.notNull(value, "Value cannot be null");

        this.map.set(new HttpHeaderName(name), value);
    }
}
