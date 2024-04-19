package com.xyzwps.lib.beans;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

sealed interface PropertyMethod {

    PropertyMethod NONE = new None();

    record None() implements PropertyMethod {
    }

    record SetMethod(Class<?> beanType, Type type, Method method,
                     String propertyName) implements PropertyMethod {
    }

    record GetMethod(Class<?> beanType, Type type, Method method,
                     String propertyName) implements PropertyMethod {
    }
}