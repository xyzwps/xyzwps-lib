package com.xyzwps.lib.beans;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

sealed interface PropertyMethod {

    PropertyMethod NONE = new None();

    record None() implements PropertyMethod {
    }

    record SetPropertyMethod(Class<?> beanType, Type type, Method method,
                             String propertyName) implements PropertyMethod {
    }

    record GetPropertyMethod(Class<?> beanType, Type type, Method method,
                             String propertyName) implements PropertyMethod {
    }
}