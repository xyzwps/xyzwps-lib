package com.xyzwps.lib.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.xyzwps.lib.beans.Utils.forEach;

record ClassAnalyzer(Class<?> beanClass) implements BeanInfoAnalyser {

    @Override
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
        return new BeanInfo(this.beanClass, getConstructor(), properties, false);
    }

    private Constructor<?> getConstructor() {
        try {
            return beanClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No default constructor in class " + beanClass.getCanonicalName(), e);
        }
    }

    private static class PropertiesCollector {
        private final Class<?> beanType;
        private final Map<String, PropertyInfo.Holder> holders = new HashMap<>();

        PropertiesCollector(Class<?> beanType) {
            this.beanType = Objects.requireNonNull(beanType);
        }

        void addMethod(Method method) {
            switch (PropertyMethodDecider.decide(beanType, method)) {
                case PropertyMethod.GetPropertyMethod m -> createOrGetHolder(m.propertyName()).addGetter(m);
                case PropertyMethod.SetPropertyMethod m -> createOrGetHolder(m.propertyName()).addSetter(m);
                case PropertyMethod.None ignored -> {
                }
            }
        }

        void addField(Field field) {
            PropertyField.create(beanType, field)
                    .ifPresent(info -> createOrGetHolder(info.getFieldName()).addField(info));
        }

        void addSuperClassMethod(Class<?> superClass, Method method) {
            switch (PropertyMethodDecider.decide(superClass, method)) {
                case PropertyMethod.GetPropertyMethod m -> createOrGetHolder(m.propertyName()).addSuperGetter(m);
                case PropertyMethod.SetPropertyMethod m -> createOrGetHolder(m.propertyName()).addSuperSetter(m);
                case PropertyMethod.None ignored -> {
                }
            }
        }

        void addSuperClassField(Class<?> superClass, Field field) {
            PropertyField.create(superClass, field)
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
