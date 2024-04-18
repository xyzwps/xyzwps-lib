package com.xyzwps.lib.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class BeanInfo {

    private final Class<?> beanClass;

    private final List<PropertyInfo> properties;

    private final Map<String, PropertyInfo> name2property;

    private final boolean isRecord;

    private final Constructor<?> constructor;

    BeanInfo(Class<?> beanClass, Constructor<?> constructor, List<PropertyInfo> properties, boolean isRecord) {
        this.beanClass = Objects.requireNonNull(beanClass);
        this.properties = List.copyOf(Objects.requireNonNull(properties));

        Map<String, PropertyInfo> n2p = new HashMap<>();
        properties.forEach(prop -> n2p.put(prop.name(), prop));
        this.name2property = Collections.unmodifiableMap(n2p);
        this.isRecord = isRecord;
        this.constructor = Objects.requireNonNull(constructor);
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public List<PropertyInfo> getBeanProperties() {
        return properties;
    }

    @SuppressWarnings("unchecked")
    public <T> T getPropertyOrNull(Object object, String propertyName) {
        var result = getProperty(object, propertyName);
        if (result instanceof GetResult.Ok it) {
            return (T) it.value();
        }
        return null;
    }

    public GetResult getProperty(Object object, String propertyName) {
        var prop = this.name2property.get(propertyName);
        return prop == null ? GetResult.noSuchProperty(propertyName) : prop.get(object);
    }

    public Optional<PropertyInfo> getPropertyInfo(String propertyName) {
        return Optional.ofNullable(this.name2property.get(propertyName));
    }

    public void setPropertyOrIgnore(Object object, String propertyName, Object value) {
        var propertyInfo = this.name2property.get(propertyName);
        if (propertyInfo != null) {
            propertyInfo.setProperty(object, value);
        }
    }

    public SetResult setProperty(Object object, String propertyName, Object value) {
        var propertyInfo = this.name2property.get(propertyName);
        return propertyInfo == null ? SetResult.NoSuchProperty(propertyName) : propertyInfo.setProperty(object, value);
    }

    public <T> T create(Map<String, Object> values) {
        Objects.requireNonNull(values);
        if (isRecord) {
            var args = this.properties.stream()
                    .map(prop -> values.get(prop.name())) // TODO: 类型安全检查
                    .toArray(Object[]::new);
            try {
                return (T) constructor.newInstance(args);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        } else {
            try {
                var obj = constructor.newInstance();
                this.properties.forEach(prop -> {
                    if (prop.writable()) {
                        prop.setProperty(obj, values.get(prop.name())); // TODO: 类型安全检查
                    }
                });
                return (T) obj;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
