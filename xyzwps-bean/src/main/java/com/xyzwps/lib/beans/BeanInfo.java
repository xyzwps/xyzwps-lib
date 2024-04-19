package com.xyzwps.lib.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class BeanInfo<T> {

    private final Class<T> beanClass;

    private final List<PropertyInfo> properties;

    private final Map<String, PropertyInfo> name2property;

    private final boolean isRecord;

    private final Constructor<T> constructor;

    BeanInfo(Class<T> beanClass, Constructor<T> constructor, List<PropertyInfo> properties, boolean isRecord) {
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

    public Object getPropertyOrNull(Object object, String propertyName) {
        var result = getProperty(object, propertyName);
        if (result instanceof GetResult.Ok it) {
            return it.value();
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
            propertyInfo.set(object, value);
        }
    }

    public SetResult setProperty(Object object, String propertyName, Object value) {
        var propertyInfo = this.name2property.get(propertyName);
        return propertyInfo == null ? SetResult.NoSuchProperty(propertyName) : propertyInfo.set(object, value);
    }

    public T create(Map<String, Object> values) {
        Objects.requireNonNull(values);
        if (isRecord) {
            var args = this.properties.stream()
                    .map(prop -> {
                        var value = values.get(prop.name());
                        return PropertySetHelper.toSettableValue(value, prop.type());
                    })
                    .toArray(Object[]::new);
            try {
                return constructor.newInstance(args);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new BeanException("Create record failed", e);
            }
        } else {
            try {
                var obj = constructor.newInstance();
                this.properties.forEach(prop -> {
                    if (prop.writable()) {
                        var value = values.get(prop.name());
                        prop.set(obj, PropertySetHelper.toSettableValue(value, prop.type()));
                    }
                });
                return obj;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new BeanException("Create object failed", e);
            }
        }
    }
}
