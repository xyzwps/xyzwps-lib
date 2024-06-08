package com.xyzwps.lib.bedrock.lang;

import java.util.List;
import java.util.Map;

public final class Equals {

    public static <T> boolean equals(List<T> a, List<T> b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (!a.getClass().equals(b.getClass())) return false;
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) return false;
        }
        return true;
    }

    public static <T> boolean itemEquals(List<T> a, List<T> b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) return false;
        }
        return true;
    }

    public static <K, V> boolean equals(Map<K, V> a, Map<K, V> b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (!a.getClass().equals(b.getClass())) return false;
        if (a.size() != b.size()) return false;
        for (var entry : a.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            if (!value.equals(b.get(key))) return false;
        }
        return true;
    }

    public static <K, V> boolean itemEquals(Map<K, V> a, Map<K, V> b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a.size() != b.size()) return false;
        for (var entry : a.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            if (!value.equals(b.get(key))) return false;
        }
        return true;
    }

    public static <T> boolean arrayEquals(T[] a, T[] b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (!a[i].equals(b[i])) return false;
        }
        return true;
    }

    private Equals() throws IllegalAccessException {
        throw new IllegalAccessException("??");
    }
}
