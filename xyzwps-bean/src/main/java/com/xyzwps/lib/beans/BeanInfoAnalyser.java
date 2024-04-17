package com.xyzwps.lib.beans;

import java.lang.reflect.Modifier;
import java.util.Objects;

interface BeanInfoAnalyser {

    BeanInfo analyse();

    static BeanInfoAnalyser create(Class<?> beanClass) {
        Objects.requireNonNull(beanClass);

        if (beanClass.isPrimitive()) {
            throw new IllegalArgumentException("Primitive type cannot play a role of bean.");
        }
        if (beanClass.isAnnotation()) {
            throw new IllegalArgumentException("Annotation cannot play a role of bean.");
        }
        if (beanClass.isInterface()) {
            throw new IllegalArgumentException("Interface cannot play a role of bean.");
        }
        if (beanClass.isEnum()) {
            throw new IllegalArgumentException("Enum cannot play a role of bean.");
        }
        if (beanClass.isRecord()) {
            return new RecordAnalyzer(beanClass);
        }
        if (Modifier.isAbstract(beanClass.getModifiers())) {
            throw new IllegalArgumentException("Abstract class cannot play a role of bean.");
        }
        if (beanClass == Object.class) {
            throw new IllegalArgumentException("Object cannot play a role of bean.");
        }

        return new ClassAnalyzer(beanClass);
    }

}
