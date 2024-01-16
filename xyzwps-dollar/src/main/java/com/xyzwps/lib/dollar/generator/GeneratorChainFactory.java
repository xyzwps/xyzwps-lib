package com.xyzwps.lib.dollar.generator;

import com.xyzwps.lib.dollar.Chain;
import com.xyzwps.lib.dollar.ChainFactory;
import com.xyzwps.lib.dollar.iterator.RangeIterable;

import java.util.Objects;

public enum GeneratorChainFactory implements ChainFactory {
    INSTANCE;

    @Override
    public <T> Chain<T> empty() {
        return new GeneratorChain<>(Generator.empty());
    }

    @Override
    public <T> Chain<T> from(Iterable<T> iterable) {
        if (iterable == null) return empty();

        return new GeneratorChain<>(Generator.create(iterable));
    }

    @SafeVarargs
    @Override
    public final <T> Chain<T> just(T... elements) {
        return new GeneratorChain<>(Generator.fromArray(elements));
    }

    @Override
    public Chain<Integer> infinite(int start) {
        return new GeneratorChain<>(Generator.infinite(start));
    }

    @Override
    public Chain<Integer> range(int start, int end) {
        return new GeneratorChain<>(Generator.create(new RangeIterable(start, end)));
    }
}
