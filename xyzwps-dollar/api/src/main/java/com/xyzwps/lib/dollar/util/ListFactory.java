package com.xyzwps.lib.dollar.util;

import java.util.*;

/**
 * A factory for creating lists.
 */
public interface ListFactory {

    /**
     * Create a list.
     *
     * @param args elements of list
     * @param <T>  type of elements
     * @return new list
     */
    @SuppressWarnings("unchecked")
    default <T> ArrayList<T> arrayList(T... args) {
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
    default <T> ArrayList<T> arrayListFrom(Iterator<T> itr) {
        return SharedUtils.arrayListFrom(itr);
    }
}
