package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.Chain;
import com.xyzwps.lib.dollar.ChainFactory;

public enum IteratorChainFactory implements ChainFactory {
    INSTANCE;

    @Override
    public <T> Chain<T> empty() {
        return new IteratorChain<>(null);
    }

    @Override
    public <T> Chain<T> from(Iterable<T> iterable) {
        return new IteratorChain<>(iterable);
    }

    @SafeVarargs
    @Override
    public final <T> Chain<T> just(T... elements) {
        return new IteratorChain<>(new ArrayIterable<>(elements));
    }

    @Override
    public Chain<Integer> infinite(int start) {
        return new IteratorChain<>(new InfiniteIterable(start));
    }

    @Override
    public Chain<Integer> range(int start, int end) {
        return new IteratorChain<>(new RangeIterable(start, end));
    }
}
