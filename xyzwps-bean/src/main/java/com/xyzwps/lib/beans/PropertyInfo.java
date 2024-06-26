package com.xyzwps.lib.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Objects;

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
     * The field of the property.
     *
     * @return the field of the property. May be null if the property is not backed by a field.
     */
    Field field();

    /**
     * Get the first annotation satisfying the predicate. The search order is field, getter, setter.
     *
     * @return the first annotations satisfying the predicate. May be null if no annotation is found.
     */
    default <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        Objects.requireNonNull(annotationClass);
        // TODO: write tests for this method
        var field = field();
        if (field != null) {
            var anno = field.getAnnotation(annotationClass);
            if (anno != null) {
                return anno;
            }
        }

        if (readable()) {
            var anno = getter().method().getAnnotation(annotationClass);
            if (anno != null) {
                return anno;
            }
        }

        if (writable()) {
            var anno = setter().method().getAnnotation(annotationClass);
            if (anno != null) {
                return anno;
            }
        }

        return null;
    }

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
