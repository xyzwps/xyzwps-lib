package com.xyzwps.lib.beans;

public sealed interface SetResult {

    SetResult OK = new Ok();

    SetResult NOT_WRITABLE = new NotWritable();

    static SetResult NoSuchProperty(String name) {
        return new NoSuchProperty(name);
    }

    static SetResult failed(Exception ex) {
        return new Failed(ex);
    }

    record Ok() implements SetResult {
    }

    record Failed(Exception cause) implements SetResult {
    }

    record NotWritable() implements SetResult {
    }

    record NoSuchProperty(String name) implements SetResult {
    }
}