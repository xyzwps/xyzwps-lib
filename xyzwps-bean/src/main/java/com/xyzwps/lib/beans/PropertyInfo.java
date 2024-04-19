package com.xyzwps.lib.beans;

import java.lang.reflect.Type;

public interface PropertyInfo {

    String name();

    Type type();

    /**
     * @return null if property is not readable
     */
    Getter getter();

    /**
     * @return null if property is not writable
     */
    Setter setter();

    boolean readable();

    boolean writable();

    Class<?> beanClass();

    /**
     * Get property value from getter, not property field.
     */
    default GetResult get(Object object) {
        if (readable()) {
            return getter().get(object);
        }
        return GetResult.NOT_READABLE;
    }

    /**
     * Set property value through setter, not property field.
     */
    default SetResult setProperty(Object object, Object value) {
        if (writable()) {
            return setter().set(object, value);
        }
        return SetResult.NOT_WRITABLE;
    }
}
