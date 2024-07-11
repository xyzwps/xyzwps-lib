package com.xyzwps.lib.dollar.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * Shared for implementations.
 */
public final class SharedUtils {

    /**
     * Implementation of {@link ObjectUtils#defaultTo(Object, Object)}.
     *
     * @param value        which to be checked
     * @param defaultValue fallback default value
     * @param <T>          type of value
     * @return value if it is not null; defaultValue if value is null
     */
    public static <T> T defaultTo(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * Implementation of {@link ObjectUtils#isFalsey(Object)}.
     *
     * @param value which to be checked
     * @return true if it would be considered as false
     */
    public static boolean isFalsey(Object value) {
        return value == null
               || Objects.equals(value, false)
               || "".equals(value)
               || Objects.equals(value, 0)
               || Objects.equals(value, 0L)
               || Objects.equals(value, 0.0)
               || Objects.equals(value, 0.0f);
    }

    /**
     * Implementation of {@link ListFactory#arrayListFrom(Iterator)}.
     *
     * @param itr provides items for result
     * @param <T> type of element
     * @return an array list
     */
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
