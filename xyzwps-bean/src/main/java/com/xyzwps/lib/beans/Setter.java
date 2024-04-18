package com.xyzwps.lib.beans;

import java.lang.reflect.Method;

public interface Setter {

    Method method();

    String propertyName();

    Class<?> beanClass();

    SetResult set(Object obj, Object value);
}
