package com.xyzwps.lib.beans;

import com.xyzwps.lib.beans.ex.BeanException;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

class PropertyInfoBuilder {

    private final String propertyName;
    private final Class<?> beanClass;
    private PropertyMethod.SetPropertyMethod setMethod;
    private PropertyMethod.GetPropertyMethod getMethod;
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
        Getter getter = getMethod == null ? null : new ImplGetter(getMethod.method(), propertyName, beanClass);
        Setter setter = setMethod == null ? null : new ImplSetter(setMethod.method(), propertyName, beanClass);
        return Optional.of(new ImplPropertyInfo(propertyName, propertyType,
                getter, setter, getMethod != null, setMethod != null, beanClass));
    }

    private Type decidePropertyType() {
        // TODO: 三方必须来自于同一个类
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
            // TODO: getter 支持父类
        }

        if (setMethod != null) {
            if (type == null) {
                type = setMethod.type();
            } else if (!type.equals(setMethod.type())) {
                throw new BeanException("Set method return type not match on property " + propertyName + "@" + beanClass.getCanonicalName());
            }
            // TODO: setter 支持子类
        }

        return type;
    }

    void addSetter(PropertyMethod.SetPropertyMethod setMethod) {
        if (this.setMethod == null) {
            this.setMethod = setMethod;
        } else {
            throw new BeanException("More than one set method on property " + propertyName + "@" + beanClass.getCanonicalName());
        }
    }

    void addGetter(PropertyMethod.GetPropertyMethod getMethod) {
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

    void addSuperGetter(PropertyMethod.GetPropertyMethod getMethod) {
        if (this.getMethod == null) {
            this.getMethod = getMethod;
        }
    }

    void addSuperSetter(PropertyMethod.SetPropertyMethod setMethod) {
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