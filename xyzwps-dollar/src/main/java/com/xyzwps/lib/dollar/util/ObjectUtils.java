package com.xyzwps.lib.dollar.util;

public final class ObjectUtils {

    public static <T> T defaultTo(T t, T defaultValue) {
        return t == null ? defaultValue : t;
    }
}
