package com.xyzwps.lib.dollar.util;

import java.util.*;

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

    /**
     * Create a list from an {@link Iterator}.
     *
     * @param itr which provide elements
     * @param <T> type of elements
     * @return new list
     */
    public static <T> ArrayList<T> arrayListFrom(Iterator<T> itr) {
        if (itr == null) {
            return new ArrayList<>();
        }

        var list = new ArrayList<T>();
        while (itr.hasNext()) list.add(itr.next());
        return list;
    }

    private ListFactory() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
