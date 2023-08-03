package com.xyzwps.lib.dollar.chain;

public sealed interface Result<T> {

    static <E> Value<E> value(E e) {
        return new Value<>(e);
    }

    static <E> Done<E> done() {
        //noinspection unchecked
        return (Done<E>) Done.INSTANCE;
    }

    final class Value<T> implements Result<T> {
        private final T value;

        public T value() {
            return value;
        }

        private Value(T value) {
            this.value = value;
        }
    }

    final class Done<T> implements Result<T> {
        private static final Done<?> INSTANCE = new Done<>();

        private Done() {
        }
    }
}
