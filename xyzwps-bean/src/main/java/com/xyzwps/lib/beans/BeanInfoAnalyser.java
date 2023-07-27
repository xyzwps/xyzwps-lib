package com.xyzwps.lib.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.xyzwps.lib.beans.PropertyMethod.*;
import static com.xyzwps.lib.beans.Utils.*;

public class BeanInfoAnalyser {

    private final Class<?> beanClass;

    public BeanInfoAnalyser(Class<?> beanClass) {
        restrict(beanClass.isPrimitive(), "Primitive type cannot play a role of bean.");
        restrict(beanClass.isAnnotation(), "Annotation cannot play a role of bean.");
        restrict(beanClass.isInterface(), "Interface cannot play a role of bean.");
        restrict(beanClass.isRecord(), "Record cannot play a role of bean.");
        restrict(beanClass.isEnum(), "Enum cannot play a role of bean.");
        restrict(Modifier.isAbstract(beanClass.getModifiers()), "Abstract class cannot play a role of bean.");
        restrict(beanClass == Object.class, "Object cannot play a role of bean.");

        this.beanClass = Objects.requireNonNull(beanClass);
    }

    private void restrict(boolean notSupport, String message) {
        if (notSupport) {
            throw new IllegalArgumentException(message);
        }
    }

    public BeanInfo analyse() {
        var collector = new PropertiesCollector(this.beanClass);

        forEach(beanClass.getDeclaredMethods(), collector::addMethod);
        forEach(beanClass.getDeclaredFields(), collector::addField);

        // handle super classes
        for (var superClass = beanClass.getSuperclass();
             superClass != Object.class;
             superClass = superClass.getSuperclass()
        ) {
            final Class<?> sc = superClass;
            forEach(sc.getDeclaredMethods(), method -> collector.addSuperClassMethod(sc, method));
            forEach(sc.getDeclaredFields(), field -> collector.addSuperClassField(sc, field));
        }

        var properties = collector.holders.values().stream()
                .map(it -> it.toPropertyInfo().orElse(null))
                .filter(Objects::nonNull)
                .toList();
        return new BeanInfo(this.beanClass, properties);
    }

    private static class PropertiesCollector {
        private final Class<?> beanType;
        private final Map<String, PropertyInfo.Holder> holders = new HashMap<>();

        PropertiesCollector(Class<?> beanType) {
            this.beanType = Objects.requireNonNull(beanType);
        }

        void addMethod(Method method) {
            switch (PropertyMethodDecider.decide(beanType, method)) {
                case GetPropertyMethod m -> createOrGetHolder(m.propertyName()).addGetter(m);
                case SetPropertyMethod m -> createOrGetHolder(m.propertyName()).addSetter(m);
                case None ignored -> {
                }
            }
        }

        void addField(Field field) {
            SpringField.create(beanType, field)
                    .ifPresent(info -> createOrGetHolder(info.getFieldName()).addField(info));
        }

        void addSuperClassMethod(Class<?> superClass, Method method) {
            switch (PropertyMethodDecider.decide(superClass, method)) {
                case GetPropertyMethod m -> createOrGetHolder(m.propertyName()).addSuperGetter(m);
                case SetPropertyMethod m -> createOrGetHolder(m.propertyName()).addSuperSetter(m);
                case None ignored -> {
                }
            }
        }

        void addSuperClassField(Class<?> superClass, Field field) {
            SpringField.create(superClass, field)
                    .ifPresent(info -> createOrGetHolder(info.getFieldName()).addSuperField(info));
        }

        private PropertyInfo.Holder createOrGetHolder(String propertyName) {
            var holder = holders.get(propertyName);
            if (holder != null) {
                return holder;
            }

            var newHolder = new PropertyInfo.Holder(beanType, propertyName);
            holders.put(propertyName, newHolder);
            return newHolder;
        }
    }

}
