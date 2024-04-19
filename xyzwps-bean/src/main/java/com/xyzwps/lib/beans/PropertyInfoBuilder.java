package com.xyzwps.lib.beans;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

class PropertyInfoBuilder {

    private final String propertyName;
    private final Class<?> beanClass;
    private PropertyMethod.SetMethod setMethod;
    private PropertyMethod.GetMethod getMethod;
    private PropertyField propertyField;


    PropertyInfoBuilder(Class<?> beanClass, String propertyName) {
        this.beanClass = Objects.requireNonNull(beanClass);
        this.propertyName = Objects.requireNonNull(propertyName);
    }

    Optional<PropertyInfo> build() {
        if (setMethod == null && getMethod == null) {
            return Optional.empty();
        }
        if ("class".equals(propertyName)) {
            return Optional.empty();
        }

        Type propertyType = decidePropertyType();
        var getter = getMethod == null ? null : new ImplGetter(getMethod.method(), propertyName, beanClass);
        var setter = setMethod == null ? null : new ImplSetter(setMethod.method(), propertyName, beanClass);
        return Optional.of(new ImplPropertyInfo(propertyName, propertyType,
                getter, setter, getMethod != null, setMethod != null, beanClass));
    }

    private Type decidePropertyType() {
        // getter, setter and field should have the same type.
        Type type = null;
        if (propertyField != null) {
            type = propertyField.field.getGenericType();
        }

        if (getMethod != null) {
            if (type == null) {
                type = getMethod.type();
            } else if (!type.equals(getMethod.type())) {
                throw new BeanException("Get method return type not match on property " + propertyName + "@" + beanClass.getCanonicalName());
            }
        }

        if (setMethod != null) {
            if (type == null) {
                type = setMethod.type();
            } else if (!type.equals(setMethod.type())) {
                throw new BeanException("Set method return type not match on property " + propertyName + "@" + beanClass.getCanonicalName());
            }
        }

        return type;
    }

    void addSetter(PropertyMethod.SetMethod setMethod) {
        if (this.setMethod == null) {
            this.setMethod = setMethod;
        } else {
            throw new BeanException("More than one set method on property " + propertyName + "@" + beanClass.getCanonicalName());
        }
    }

    void addGetter(PropertyMethod.GetMethod getMethod) {
        if (this.getMethod == null) {
            this.getMethod = getMethod;
        } else {
            throw new BeanException("More than one get method on property " + propertyName + "@" + beanClass.getCanonicalName());
        }
    }

    void addField(PropertyField propertyField) {
        if (this.propertyField == null) {
            this.propertyField = propertyField;
        }
    }

    void addSuperGetter(PropertyMethod.GetMethod getMethod) {
        if (this.getMethod == null) {
            this.getMethod = getMethod;
        }
    }

    void addSuperSetter(PropertyMethod.SetMethod setMethod) {
        if (this.setMethod == null) {
            this.setMethod = setMethod;
        }
    }

    void addSuperField(PropertyField field) {
        if (this.propertyField == null) {
            this.propertyField = field;
        }
    }
}