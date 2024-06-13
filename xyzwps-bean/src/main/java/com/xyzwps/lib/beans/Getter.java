package com.xyzwps.lib.beans;

import java.lang.reflect.Method;

/**
 * A getter of a property.
 */
public interface Getter {

    /**
     * The get method of the property.
     *
     * @return the get method of the property.
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
     * Get property value from object.
     *
     * @param obj the object to get property value from.
     * @return the result of getting property value.
     */
    GetResult get(Object obj);

}
