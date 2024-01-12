package com.xyzwps.lib.dollar.sequence;

import java.util.*;
import java.util.function.Supplier;

public sealed interface Sequence<T> {

    final class Cons<T> implements Sequence<T> {
        private final T value;
        private final Supplier<Sequence<T>> getRest;

        Cons(T value, Sequence<T> rest) {
            Objects.requireNonNull(rest);
            this.value = value;
            this.getRest = () -> rest;
        }

        Cons(T value, Supplier<Sequence<T>> restSupplier) {
            this.value = value;
            this.getRest = Objects.requireNonNull(restSupplier);
        }

        public T value() {
            return value;
        }

        public Sequence<T> rest() {
            return getRest.get();
        }
    }

    @SuppressWarnings("rawtypes")
    enum Nil implements Sequence {
        INSTANCE
    }

    @SuppressWarnings("unchecked")
    static <T> Sequence<T> nil() {
        return Nil.INSTANCE;
    }

    static <T> Sequence<T> from(Iterable<T> iterable) {
        return iterable == null ? nil() : from(iterable.iterator());
    }

    static <T> Sequence<T> from(Iterator<T> iterator) {
        if (iterator == null) return nil();

        return iterator.hasNext() ? new Cons<>(iterator.next(), from(iterator)) : nil();
    }

    static <T> Sequence<Long> infinite(long start) {
        return new Cons<>(start, () -> infinite(start + 1));
    }

    // TODO: range
}
