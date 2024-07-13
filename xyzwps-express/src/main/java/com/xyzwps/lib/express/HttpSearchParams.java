package com.xyzwps.lib.express;

import com.xyzwps.lib.bedrock.lang.DefaultValues;
import com.xyzwps.lib.express.util.SimpleMultiValuesMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class HttpSearchParams extends SimpleMultiValuesMap<String, String> {

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

    @SuppressWarnings("unchecked")
    public <T> T get(String name, Class<T> type) {
        // TODO: 类型转化
        var value = this.get(name);
        if (value == null) {
            return (T) DefaultValues.get(type);
        }

        if (type == String.class) {
            return (T) value;
        }
        if (type == Short.class || type == short.class) {
            return (T) Short.valueOf(value);
        }
        if (type == Integer.class || type == int.class) {
            return (T) Integer.valueOf(value);
        }
        if (type == Long.class || type == long.class) {
            return (T) Long.valueOf(value);
        }
        if (type == Double.class || type == double.class) {
            return (T) Double.valueOf(value);
        }
        if (type == Float.class || type == float.class) {
            return (T) Float.valueOf(value);
        }
        if (type == Boolean.class || type == boolean.class) {
            return (T) Boolean.valueOf(value);
        }
        if (type == BigInteger.class) {
            return (T) new BigInteger(value);
        }
        if (type == BigDecimal.class) {
            return (T) new BigDecimal(value);
        }
        if (type.isEnum()) {
            return (T) Enum.valueOf((Class<Enum>) type, value);
        }

        throw new UnsupportedOperationException("Unsupported type: " + type.getName());
    }

    private static String decode(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

    private static String encode(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    public String toHumanReadableString() {
        var sb = new StringBuilder();
        var env = new Env();
        this.forEach((name, values) -> {
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
