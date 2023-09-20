package com.xyzwps.lib.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public sealed interface PropertyMethod {

    PropertyMethod NONE = new None();

    record None() implements PropertyMethod {
    }

    record SetPropertyMethod(Class<?> beanType,
                             Method method,
                             AccessLevel accessLevel,
                             Class<?> propertyType,
                             String propertyName
    ) implements PropertyMethod {

        public SetResult setValue(Object object, Object value) {
            try {
                this.method.invoke(object, value);
                return SetResult.OK;
            } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
                return SetResult.failed(e);
            }
        }
    }

    record GetPropertyMethod(Class<?> beanType,
                             Method method,
                             AccessLevel accessLevel,
                             Class<?> propertyType,
                             String propertyName
    ) implements PropertyMethod {

        public GetResult getValue(Object object) {
            try {
                return GetResult.ok(this.method.invoke(object));
            } catch (Exception e) {
                return GetResult.failed(e);
            }
        }
    }
}