package com.xyzwps.lib.express;

import com.xyzwps.lib.express.util.SimpleMultiValuesMap;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class HttpSearchParams extends SimpleMultiValuesMap {

    public HttpSearchParams() {
        super(false);
    }

    public static HttpSearchParams parse(String rawQuery) {
        var params = new HttpSearchParams();

        if (rawQuery == null || rawQuery.isEmpty()) {
            return params;
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
            params.append(name, value);
        }
        return params;
    }

    private static String decode(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

    private static String encode(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    public String toHString() {
        var sb = new StringBuilder();
        var env = new Env();
        this.forEach((name, values) -> {
            for (var value : values) {
                if (env.first) env.first = false;
                else sb.append('&');

                sb.append(encode(name)).append('=').append(encode(value));
            }
        });
        return sb.toString();
    }

    private static class Env {
        boolean first = true;
    }
}
