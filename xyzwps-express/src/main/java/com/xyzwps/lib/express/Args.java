package com.xyzwps.lib.express;

public final class Args {

    public static void notEmpty(String str, String message) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }


    private Args() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
