package com.xyzwps.lib.beans;

/**
 * The result of getting property value.
 */
public sealed interface GetResult {

    /**
     * Get the property value or throw exception.
     *
     * @return the property value.
     */
    Object getOrThrow();

    /**
     * Create a successful result.
     *
     * @param object the property value.
     * @return the successful result.
     */
    static GetResult ok(Object object) {
        return new Ok(object);
    }

    /**
     * Create a failed result.
     *
     * @param cause the exception cause.
     * @return the failed result.
     */
    static GetResult failed(Exception cause) {
        return new Failed(cause);
    }

    /**
     * A not readable result.
     */
    GetResult NOT_READABLE = new NotReadable();

    /**
     * Create a no such property result.
     *
     * @param name the property name.
     * @return the no such property result.
     */
    static GetResult noSuchProperty(String name) {
        return new NoSuchProperty(name);
    }

    /**
     * The property value.
     *
     * @param value the property value.
     */
    record Ok(Object value) implements GetResult {
        @Override
        public Object getOrThrow() {
            return value;
        }
    }

    /**
     * The exception cause.
     *
     * @param cause the exception cause.
     */
    record Failed(Exception cause) implements GetResult {
        @Override
        public Object getOrThrow() {
            throw new BeanException(cause);
        }
    }

    /**
     * A not readable result.
     */
    record NotReadable() implements GetResult {
        @Override
        public Object getOrThrow() {
            throw new BeanException("The property is not readable.");
        }
    }

    /**
     * A no such property result.
     *
     * @param name the property name.
     */
    record NoSuchProperty(String name) implements GetResult {
        @Override
        public Object getOrThrow() {
            throw new BeanException("No such property: " + name);
        }
    }

}
