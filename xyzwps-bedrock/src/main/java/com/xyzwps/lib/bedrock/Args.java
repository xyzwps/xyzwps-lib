package com.xyzwps.lib.bedrock;

import java.util.Collection;
import java.util.List;
import java.util.function.IntFunction;

public final class Args {

    public static int gt(int value, int bound, String message) {
        if (value <= bound) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static int ge(int value, int bound, String message) {
        if (value < bound) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static void notEmpty(String str, String message) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> c, String message) {
        if (c == null || c.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> T notNull(T object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
        return object;
    }

    /**
     * @param list      you should make sure that it is not null
     * @param toMessage generate message
     * @param <T>       item type
     * @return argument list
     */
    public static <T> List<T> itemsNotNull(List<T> list, IntFunction<String> toMessage) {
        Args.notNull(list, "Argument list CANNOT be null");
        Args.notNull(toMessage, "Argument toMessage CANNOT be null");
        int i = 0;
        for (var it : list) {
            if (it == null) {
                throw new IllegalArgumentException(toMessage.apply(i));
            }
            i++;
        }
        return list;
    }

    // TODO: 修正
    @Deprecated
    public static <T> void allNotNull(T[] arr, String message) {
        if (arr == null) {
            throw new IllegalArgumentException(message);
        }
        for (var it : arr) {
            if (it == null) {
                throw new IllegalArgumentException(message);
            }
        }
    }


    private Args() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
