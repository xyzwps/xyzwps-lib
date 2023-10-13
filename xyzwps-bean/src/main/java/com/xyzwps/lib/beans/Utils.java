package com.xyzwps.lib.beans;

import java.util.function.Consumer;

final class Utils {

    static <T> void forEach(T[] arr, Consumer<T> consumer) {
        if (arr == null) return;

        for (var t : arr) consumer.accept(t);
    }
}
