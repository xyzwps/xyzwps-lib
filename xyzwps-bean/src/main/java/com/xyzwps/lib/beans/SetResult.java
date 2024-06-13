package com.xyzwps.lib.beans;

/**
 * The result of setting a property.
 */
public sealed interface SetResult {

    /**
     * The result of setting the property is OK.
     */
    SetResult OK = new Ok();

    /**
     * The property is not writable.
     */
    SetResult NOT_WRITABLE = new NotWritable();

    /**
     * The property does not exist.
     */
    static SetResult noSuchProperty(String name) {
        return new NoSuchProperty(name);
    }

    /**
     * The result of setting the property is failed.
     *
     * @param cause the exception.
     */
    static SetResult failed(Exception cause) {
        return new Failed(cause);
    }

    /**
     * The result of setting the property is OK.
     */
    record Ok() implements SetResult {
    }

    /**
     * The result of setting the property is failed.
     *
     * @param cause the exception.
     */
    record Failed(Exception cause) implements SetResult {
    }

    /**
     * The property is not writable.
     */
    record NotWritable() implements SetResult {
    }

    /**
     * The property does not exist.
     *
     * @param name the name of the property.
     */
    record NoSuchProperty(String name) implements SetResult {
    }
}