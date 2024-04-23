package com.xyzwps.lib.express;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class HttpSearchParams {

    private final Map<String, List<String>> map;

    private HttpSearchParams() {
        this.map = new TreeMap<>();
    }

    public Optional<String> getFirst(String name) {
        return map.get(name).stream().findFirst();
    }

    public List<String> getAll(String name) {
        var list = map.get(name);
        return list == null ? List.of() : list;
    }

    public static HttpSearchParams parse(String rawQuery) {
        var sp = new HttpSearchParams();

        if (rawQuery == null || rawQuery.isEmpty()) {
            return sp;
        }

        var segments = rawQuery.split("&");
        for (var it : segments) {
            if (it.isBlank()) continue;

            var i = it.indexOf('=');
            String name, value;
            if (i == -1) {
                name = decode(it);
                value = "";
            } else if (i == 0) {
                name = "";
                value = decode(it.substring(1));
            } else {
                name = decode(it.substring(0, i));
                value = decode(it.substring(i + 1));
            }
            sp.map.computeIfAbsent(name, (x) -> new LinkedList<>()).add(value);
        }
        sp.map.replaceAll((k, v) -> List.copyOf(sp.map.get(k)));
        return sp;
    }

    private static String decode(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

    public String toHString() {
        var sb = new StringBuilder();
        var env = new Env();
        map.forEach((name, values) -> {
            for (var value : values) {
                if (env.first) env.first = false;
                else sb.append('&');

                sb.append(name).append('=').append(value);
            }
        });
        return sb.toString();
    }

    private static class Env {
        boolean first = true;
    }
}
