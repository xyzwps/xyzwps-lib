package com.xyzwps.lib.express;

public final class Args {

    public static void notEmpty(String str, String message) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> T notNull(T object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
        return object;
    }

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
