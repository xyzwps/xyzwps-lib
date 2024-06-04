package com.xyzwps.lib.dollar.seq;

import com.xyzwps.lib.dollar.Chain;
import com.xyzwps.lib.dollar.ChainFactory;
import com.xyzwps.lib.dollar.util.RangeIterable;

/**
 * A factory for creating {@link SeqChain} instances.
 */
public enum SeqChainFactory implements ChainFactory {
    /**
     * The singleton instance.
     */
    INSTANCE;

    @Override
    public <T> Chain<T> empty() {
        return new SeqChain<>(null);
    }

    @Override
    public <T> Chain<T> from(Iterable<T> iterable) {
        return new SeqChain<>(Seq.from(iterable));
    }

    @SafeVarargs
    @Override
    public final <T> Chain<T> just(T... elements) {
        return new SeqChain<>(Seq.just(elements));
    }

    @Override
    public Chain<Integer> infinite(int start) {
        return new SeqChain<>(Seq.infinite(start));
    }

    @Override
    public Chain<Integer> range(int start, int end) {
        return from(new RangeIterable(start, end));
    }
}
