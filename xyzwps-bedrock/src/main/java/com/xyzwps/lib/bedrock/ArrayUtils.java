package com.xyzwps.lib.bedrock;

import java.util.function.Consumer;

public final class ArrayUtils {

    public static <T> void forEach(T[] arr, Consumer<T> consumer) {
        if (arr == null) return;

        for (var t : arr) consumer.accept(t);
    }

    private ArrayUtils() throws IllegalAccessException {
        throw new IllegalAccessException("??");
    }
}
