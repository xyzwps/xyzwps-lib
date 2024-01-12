package com.xyzwps.lib.dollar.sequence;

public sealed interface Current<T> {
    record Value<T>(T value) implements Current<T> {
    }

    @SuppressWarnings("rawtypes")
    enum End implements Current {
        INSTANCE
    }

    @SuppressWarnings("unchecked")
    static <T> Current<T> end() {
        return End.INSTANCE;
    }
}
