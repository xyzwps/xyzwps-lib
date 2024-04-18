package com.xyzwps.lib.beans;

import java.lang.reflect.Method;

public interface Getter {

    Method method();

    String propertyName();

    Class<?> beanClass();

    GetResult get(Object obj);

}
