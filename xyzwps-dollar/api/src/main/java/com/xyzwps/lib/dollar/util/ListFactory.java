package com.xyzwps.lib.dollar.util;

import java.util.ArrayList;
import java.util.Arrays;

public final class ListFactory {

    /**
     * Create a list.
     *
     * @param args elements of list
     * @param <T>  type of elements
     * @return new list
     */
    @SafeVarargs
    public static <T> ArrayList<T> arrayList(T... args) {
        var list = new ArrayList<T>(args.length);
        list.addAll(Arrays.asList(args));
        return list;
    }


    private ListFactory() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
