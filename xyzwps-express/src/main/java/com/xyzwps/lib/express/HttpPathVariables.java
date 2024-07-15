package com.xyzwps.lib.express;

import com.xyzwps.lib.bedrock.lang.DefaultValues;
import com.xyzwps.lib.dollar.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class HttpPathVariables {
    private final Map<String, String> data = new HashMap<>();

    public void add(String name, String value) {
        this.data.put(name, value);
    }

    public String get(String name) {
        return this.data.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name, Class<T> clazz) {
        var value = this.get(name);
        if (value == null) {
            return (T) DefaultValues.get(clazz);
        }

        if (clazz == String.class) {
            return (T) value;
        }
        if (clazz == Short.class || clazz == short.class) {
            return (T) Short.valueOf(value);
        }
        if (clazz == Integer.class || clazz == int.class) {
            return (T) Integer.valueOf(value);
        }
        if (clazz == Long.class || clazz == long.class) {
            return (T) Long.valueOf(value);
        }
        if (clazz == Float.class || clazz == float.class) {
            return (T) Float.valueOf(value);
        }
        if (clazz == Double.class || clazz == double.class) {
            return (T) Double.valueOf(value);
        }
        if (clazz == Boolean.class || clazz == boolean.class) {
            return (T) Boolean.valueOf(value);
        }
        if (clazz == BigInteger.class) {
            return (T) new BigInteger(value);
        }
        if (clazz == BigDecimal.class) {
            return (T) new BigDecimal(value);
        }

        throw new UnsupportedOperationException("Unsupported path variable type: " + clazz.getCanonicalName());
    }

    public Set<String> names() {
        return data.keySet();
    }

    public void addAll(List<Pair<String, String>> pathVariables) {
        if (pathVariables != null) {
            for (var pair : pathVariables) {
                this.add(pair.first(), pair.second());
            }
        }
    }
}
