package com.xyzwps.lib.dollar.sequence;

public sealed interface NextResult<T> {
    record Value<T>(T value) implements NextResult<T> {
    }

    @SuppressWarnings("rawtypes")
    enum End implements NextResult {
        INSTANCE
    }

    @SuppressWarnings("unchecked")
    static <T> NextResult<T> end() {
        return End.INSTANCE;
    }
}
