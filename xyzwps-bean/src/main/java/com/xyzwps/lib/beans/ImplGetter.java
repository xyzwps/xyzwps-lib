package com.xyzwps.lib.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

record ImplGetter(Method method, String propertyName, Class<?> beanClass) implements Getter {
    ImplGetter {
        Objects.requireNonNull(method);
        Objects.requireNonNull(propertyName);
        Objects.requireNonNull(beanClass);
    }

    @Override
    public GetResult get(Object object) {
        try {
            return GetResult.ok(this.method.invoke(object));
        } catch (InvocationTargetException | IllegalAccessException e) {
            return GetResult.failed(e);
        }
    }
}