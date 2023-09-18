package com.xyzwps.lib.beans;

import java.util.function.Supplier;

public class UnexpectedException extends RuntimeException {
    public UnexpectedException(String message) {
        super(message);
    }

    public static Supplier<UnexpectedException> unexpected(String message) {
        return () -> new UnexpectedException(message);
    }
}
