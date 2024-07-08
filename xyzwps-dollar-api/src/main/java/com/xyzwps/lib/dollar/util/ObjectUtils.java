package com.xyzwps.lib.dollar.util;

/**
 * A utility class for object operations.
 */
public interface ObjectUtils {

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
    default <T> T defaultTo(T value, T defaultValue) {
        return SharedUtils.defaultTo(value, defaultValue);
    }

    /**
     * Check value which is falsey or not. The values <code>null</code>, <code>false</code>,
     * <code>0(.0)</code> and <code>""</code> are falsey.
     *
     * @param value value to be checked
     * @return true if the value is falsey
     */
    default boolean isFalsey(Object value) {
        return SharedUtils.isFalsey(value);
    }
}
