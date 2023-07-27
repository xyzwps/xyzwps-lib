package com.xyzwps.lib.json.element;

import java.util.HashMap;
import java.util.Map;

public final class JsonObject implements JsonElement {
    private final Map<String, JsonElement> map = new HashMap<>();

    public void put(String property, JsonElement element) {
        this.map.put(property, element);
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
}
