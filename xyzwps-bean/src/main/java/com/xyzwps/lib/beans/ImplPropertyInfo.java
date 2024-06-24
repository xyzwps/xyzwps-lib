package com.xyzwps.lib.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Objects;

record ImplPropertyInfo(String name, Type type, Field field,
                        Getter getter, Setter setter,
                        boolean readable, boolean writable,
                        Class<?> beanClass
) implements PropertyInfo {

    ImplPropertyInfo {
        Objects.requireNonNull(beanClass);
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);

        if (getter == null && setter == null) {
            throw new IllegalArgumentException("There should be at least one of getter or setter.");
        }
    }

}
