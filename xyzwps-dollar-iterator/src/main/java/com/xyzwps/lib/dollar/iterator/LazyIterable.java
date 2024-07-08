package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.util.SharedUtils;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;

import static com.xyzwps.lib.dollar.util.ObjectUtils.*;

public class LazyIterable<T> implements Iterable<T> {

    private final Supplier<Iterable<T>> supplier;

    public LazyIterable(Supplier<Iterable<T>> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }

    private Iterable<T> iterable;

    @Override
    public Iterator<T> iterator() {
        if (iterable == null) {
            iterable = SharedUtils.defaultTo(supplier.get(), EmptyIterable.create());
        }
        return iterable.iterator();
    }
}
