package com.xyzwps.lib.beans;

import java.lang.reflect.Method;

/**
 * A setter for a property.
 */
public interface Setter {

    /**
     * The set method of the property.
     *
     * @return the set method of the property.
     */
    Method method();

    /**
     * The name of the property.
     *
     * @return the name of the property.
     */
    String propertyName();

    /**
     * The bean class of the property.
     *
     * @return the bean class of the property.
     */
    Class<?> beanClass();

    /**
     * Set the property of the object to the value.
     *
     * @param obj   the object.
     * @param value the value.
     * @return the result of setting the property.
     */
    SetResult set(Object obj, Object value);
}
