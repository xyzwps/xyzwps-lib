package com.xyzwps.lib.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The bean information.
 *
 * @param <T> the type of the bean.
 */
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

    /**
     * Get the class of the bean.
     *
     * @return the class of the bean.
     */
    public Class<?> getBeanClass() {
        return beanClass;
    }

    /**
     * Get the properties of the bean.
     *
     * @return the properties of the bean.
     */
    public List<PropertyInfo> getBeanProperties() {
        return properties;
    }

    /**
     * Get the property names of the bean.
     *
     * @return the property names of the bean.
     */
    public Object getPropertyOrNull(Object object, String propertyName) {
        var result = getProperty(object, propertyName);
        if (result instanceof GetResult.Ok it) {
            return it.value();
        }
        return null;
    }

    /**
     * Get the property value.
     *
     * @param object       the object to get property value from
     * @param propertyName the name of the property
     * @return the result of getting property value
     */
    public GetResult getProperty(Object object, String propertyName) {
        var prop = this.name2property.get(propertyName);
        return prop == null ? GetResult.noSuchProperty(propertyName) : prop.get(object);
    }

    /**
     * Get the property information.
     *
     * @param propertyName the name of the property
     * @return the property information
     */
    public Optional<PropertyInfo> getPropertyInfo(String propertyName) {
        return Optional.ofNullable(this.name2property.get(propertyName));
    }

    /**
     * Set property or ignore.
     *
     * @param object       the object to set property value to
     * @param propertyName the name of the property
     * @param value        the value to set
     */
    public void setPropertyOrIgnore(Object object, String propertyName, Object value) {
        var propertyInfo = this.name2property.get(propertyName);
        if (propertyInfo != null) {
            propertyInfo.set(object, value);
        }
    }

    /**
     * Set property.
     *
     * @param object       the object to set property value to
     * @param propertyName the name of the property
     * @param value        the value to set
     * @return the result of setting property
     */
    public SetResult setProperty(Object object, String propertyName, Object value) {
        var propertyInfo = this.name2property.get(propertyName);
        return propertyInfo == null ? SetResult.noSuchProperty(propertyName) : propertyInfo.set(object, value);
    }

    /**
     * Create a new bean instance.
     *
     * @param values the property values
     * @return the new bean instance
     */
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
