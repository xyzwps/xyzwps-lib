package com.xyzwps.lib.beans;

import java.util.*;

public class BeanInfo {

    private final Class<?> beanClass;

    private final List<PropertyInfo> properties;

    private final Map<String, PropertyInfo> name2property;

    public BeanInfo(Class<?> beanClass, List<PropertyInfo> properties) {
        this.beanClass = Objects.requireNonNull(beanClass);
        this.properties = Collections.unmodifiableList(Objects.requireNonNull(properties));

        Map<String, PropertyInfo> n2p = new HashMap<>();
        properties.forEach(it -> n2p.put(it.getPropertyName(), it));
        this.name2property = Collections.unmodifiableMap(n2p);
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
        var propertyInfo = this.name2property.get(propertyName);
        return propertyInfo == null ? GetResult.noSuchProperty(propertyName) : propertyInfo.getProperty(object);
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
}
