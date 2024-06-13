package com.xyzwps.lib.beans;

import java.lang.reflect.Type;

/**
 * A property of a bean.
 */
public interface PropertyInfo {

    /**
     * The name of the property.
     *
     * @return the name of the property.
     */
    String name();

    /**
     * The type of the property.
     *
     * @return the type of the property.
     */
    Type type();

    /**
     * @return null if property is not readable
     */
    Getter getter();

    /**
     * @return null if property is not writable
     */
    Setter setter();

    /**
     * Check if the property is readable.
     *
     * @return true if property is readable
     */
    boolean readable();

    /**
     * Check if the property is writable.
     *
     * @return true if property is writable
     */
    boolean writable();

    /**
     * The bean class of the property.
     *
     * @return the bean class of the property.
     */
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
    default SetResult set(Object object, Object value) {
        if (writable()) {
            try {
                return setter().set(object, PropertySetHelper.toSettableValue(value, type()));
            } catch (Exception e) {
                return SetResult.failed(e);
            }
        }
        return SetResult.NOT_WRITABLE;
    }
}
