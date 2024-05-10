package com.xyzwps.lib.dollar.util;

import java.util.Objects;

public final class ObjectUtils {

    /**
     * Checks <code>value</code> to determine whether a public static value
     * should be returned in its place. The <code>defaultValue</code>
     * is returned when <code>value</code> is <code>null</code>.
     *
     * @param value        The value to check
     * @param defaultValue The public static value
     * @param <T>          value type
     * @return resolved value
     */
    public static <T> T defaultTo(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * Check value which is falsey or not. The values <code>null</code>, <code>false</code>,
     * <code>0(.0)</code> and <code>""</code> are falsey.
     *
     * @param value value to be checked
     * @return true if the value is falsey
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

    private ObjectUtils() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
