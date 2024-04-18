package com.xyzwps.lib.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import static com.xyzwps.lib.beans.PropertyMethod.*;

record ImplSetter(Method method, String propertyName, Class<?> beanClass) implements Setter {
    ImplSetter {
        Objects.requireNonNull(method);
        Objects.requireNonNull(propertyName);
        Objects.requireNonNull(beanClass);
    }

    @Override
    public SetResult set(Object obj, Object value) {
        try {
            this.method.invoke(obj, value);
            return SetResult.OK;
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            return SetResult.failed(e);
        }
    }
}