package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.seq.SeqChainFactory;
import com.xyzwps.lib.dollar.seq.SeqMapEntryChainFactory;

import java.util.*;

/**
 * Where to start.
 * <p>
 * TODO: optimize for RandomAccess
 * TODO: 写中文文档
 * TODO: add examples
 * TODO: all apis should be null tolerant
 * TODO: 探索怎么能把这东西搞优雅了
 */
public final class Dollar {

    private static final ChainFactory cf = SeqChainFactory.INSTANCE;
    private static final MapEntryChainFactory mf = SeqMapEntryChainFactory.INSTANCE;


    /**
     * Create a stage chain from a {@link List}.
     *
     * @param list source list. Null is acceptable.
     * @param <T>  list element type
     * @return a list stage
     */
    public static <T> Chain<T> $(Iterable<T> list) {
        return cf.from(list);
    }


    /**
     * Create a stage chain from a map.
     *
     * @param map source map. Null is acceptable.
     * @param <K> map key type
     * @param <V> map value type
     * @return a map stage
     */
    public static <K, V> MapEntryChain<K, V> $(Map<K, V> map) {
        return mf.from(map);
    }


    /**
     * Dollar functions.
     */
    public static final DollarFunctions $ = new DollarFunctions(cf, mf);


    private Dollar() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
