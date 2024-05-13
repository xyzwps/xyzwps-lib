package com.xyzwps.lib.dollar.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public final class SharedUtils {

    public static <T> T defaultTo(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static boolean isFalsey(Object value) {
        return value == null
               || Objects.equals(value, false)
               || "".equals(value)
               || Objects.equals(value, 0)
               || Objects.equals(value, 0L)
               || Objects.equals(value, 0.0)
               || Objects.equals(value, 0.0f);
    }

    public static <T> ArrayList<T> arrayListFrom(Iterator<T> itr) {
        if (itr == null) {
            return new ArrayList<>();
        }

        var list = new ArrayList<T>();
        while (itr.hasNext()) list.add(itr.next());
        return list;
    }

    private SharedUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
