package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.util.*;

import java.util.Objects;

public final class DollarFunctions implements ObjectUtils, CollectionUtils, MapUtils, StringUtils, ListFactory, MapFactory {

    private final ChainFactory cf;
    private final MapEntryChainFactory mf;

    DollarFunctions(ChainFactory cf, MapEntryChainFactory mf) {
        this.cf = Objects.requireNonNull(cf);
        this.mf = Objects.requireNonNull(mf);
    }


    /**
     * Create an empty list stage.
     *
     * @param <T> element type
     * @return list stage
     */
    public <T> Chain<T> empty() {
        return cf.empty();
    }


    /**
     * Create a stage from elements.
     *
     * @param args elements to be handled
     * @param <T>  type of elements
     * @return list stage
     */
    @SafeVarargs
    public final <T> Chain<T> just(T... args) {
        return cf.just(args);
    }


    /**
     * Handle a range.
     *
     * @param start range start - included
     * @param end   range end - excluded
     * @return list stage
     */
    public Chain<Integer> range(int start, int end) {
        return cf.range(start, end);
    }

}
