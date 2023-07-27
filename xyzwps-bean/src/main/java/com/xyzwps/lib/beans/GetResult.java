package com.xyzwps.lib.beans;

public sealed interface GetResult {

    static GetResult ok(Object object) {
        return new Ok(object);
    }

    static GetResult failed(Exception ex) {
        return new Failed(ex);
    }

    GetResult NOT_READABLE = new NotReadable();

    static GetResult noSuchProperty(String name) {
        return new NoSuchProperty(name);
    }

    record Ok(Object value) implements GetResult {
    }

    record Failed(Exception cause) implements GetResult {
    }

    record NotReadable() implements GetResult {
    }

    record NoSuchProperty(String name) implements GetResult {
    }

}
