package com.xyzwps.lib.json.element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public final class JsonObject implements JsonElement {
    private final Map<String, JsonElement> map = new HashMap<>();

    public void put(String property, JsonElement element) {
        this.map.put(property, element);
    }

    public JsonElement get(String property) {
        return this.map.get(property);
    }

    public JsonObject put(String property, String str) {
        this.map.put(Objects.requireNonNull(property), str == null ? JsonNull.INSTANCE : new JsonString(str));
        return this;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder().append('{');
        var env = new Env();
        map.forEach((key, value) -> {
            if (env.first) env.first = false;
            else sb.append(',');

            sb.append('"').append(key) // TODO: 处理转义字符
                    .append('"').append(':').append(value.toString());
        });

        return sb.append('}').toString();
    }

    private static class Env {
        boolean first = true;
    }

    public void forEach(BiConsumer<String, JsonElement> action) {
        map.forEach(action);
    }
}
