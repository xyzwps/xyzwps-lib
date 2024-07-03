package com.xyzwps.lib.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

record ImplSetter(Method method, String propertyName, Class<?> beanClass) implements Setter {
    ImplSetter {
        Objects.requireNonNull(method);
        Objects.requireNonNull(propertyName);
        Objects.requireNonNull(beanClass);
    }

    @Override
    public SetResult set(Object obj, Object value) {
        try {
            this.method.setAccessible(true); // TODO: 可不可以只 set 一次
            this.method.invoke(obj, value);
            return SetResult.OK;
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            return SetResult.failed(e);
        }
    }
}