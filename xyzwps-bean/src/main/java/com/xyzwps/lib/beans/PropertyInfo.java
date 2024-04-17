package com.xyzwps.lib.beans;

import com.xyzwps.lib.beans.ex.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.xyzwps.lib.beans.PropertyMethod.*;

public record PropertyInfo(String propertyName, Type propertyType,
                           PropertyGetter propertyGetter, PropertySetter propertySetter,
                           Class<?> beanType) {
    public PropertyInfo {
        Objects.requireNonNull(beanType);
        Objects.requireNonNull(propertyName);
        Objects.requireNonNull(propertyType);

        if (propertyGetter == null && propertySetter == null) {
            throw new IllegalArgumentException("There should be at least one of GetterInfo and SetterInfo.");
        }
    }

    public boolean readable() {
        return propertyGetter != null && propertyGetter.isReadable();
    }

    public boolean writable() {
        return propertySetter != null && propertySetter.isWritable();
    }


    public GetResult getProperty(Object object) {
        return propertyGetter.get(object);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPropertyOrThrow(Object object, Class<T> tClass) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(tClass);
        return switch (propertyGetter.get(object)) {
            case GetResult.NoSuchProperty ignored -> throw new NoSuchPropertyException(this);
            case GetResult.Failed failed -> throw new UnhandledBeanException(failed.cause());
            case GetResult.NotReadable ignored -> throw new PropertyIsNotReadableException(this);
            case GetResult.Ok ok -> {
                var value = ok.value();
                try {
                    yield (T) TryBestToGetProperty.INSTANCE.tryToGet(this.propertyType, tClass, value);
                } catch (Exception e) {
                    throw new PropertyCaseException(this, tClass);
                }
            }
        };
    }


    public SetResult setProperty(Object object, Object value) {
        return propertySetter.set(object, TryBestToSetProperty.INSTANCE.convertToSet(this.propertyType, value));
    }

    public void setPropertyOrThrow(Object object, Object value) {
        Objects.requireNonNull(object);
        switch (this.setProperty(object, value)) {
            case SetResult.Failed failed -> throw new UnhandledBeanException(failed.cause());
            case SetResult.NoSuchProperty ignored -> throw new NoSuchPropertyException(this);
            case SetResult.NotWritable ignored -> throw new PropertyIsNotWritableException(this);
            case SetResult.Ok ignored -> {
            }
        }
    }

    // TODO: 处理注解

    public static class Holder {

        private final String propertyName;
        private final Class<?> beanType;
        private SetPropertyMethod setMethod;
        private GetPropertyMethod getMethod;
        private PropertyField propertyField;


        public Holder(Class<?> beanType, String propertyName) {
            this.beanType = Objects.requireNonNull(beanType);
            this.propertyName = Objects.requireNonNull(propertyName);
        }

        public Optional<PropertyInfo> toPropertyInfo() {
            if (setMethod == null && getMethod == null) {
                return Optional.empty();
            }
            if ("class".equals(propertyName)) {
                return Optional.empty();
            }

            var propertyType = decidePropertyType();
            var getterInfo = PropertyGetter.create(getMethod, propertyField);
            var setterInfo = PropertySetter.create(setMethod, propertyField);
            return Optional.of(new PropertyInfo(propertyName, propertyType, getterInfo, setterInfo, beanType));
        }


        private Type decidePropertyType() {
            // TODO: 三方类型必须相同
            // TODO: 三方必须来自于同一个类

            Type propertyType = null;
            if (propertyField != null) {
                propertyType = propertyField.getFieldType();
            }

            if (getMethod != null) {
                if (propertyType == null) {
                    propertyType = getMethod.propertyType();
                } else if (propertyType != getMethod.propertyType()) {
                    throw new BeanException("Get method return type not match on property " + propertyName + "@" + beanType.getCanonicalName());
                }
                // TODO: getter 支持父类
            }

            if (setMethod != null) {
                if (propertyType == null) {
                    propertyType = setMethod.propertyType();
                } else if (propertyType != setMethod.propertyType()) {
                    throw new BeanException("Set method return type not match on property " + propertyName + "@" + beanType.getCanonicalName());
                }
                // TODO: setter 支持子类
            }


            return propertyType;
        }

        public void addSetter(SetPropertyMethod setMethod) {
            if (this.setMethod == null) {
                this.setMethod = setMethod;
            } else {
                throw new BeanException("More than one set method on property " + propertyName + "@" + beanType.getCanonicalName());
            }
        }

        public void addGetter(GetPropertyMethod getMethod) {
            if (this.getMethod == null) {
                this.getMethod = getMethod;
            } else {
                throw new BeanException("More than one get method on property " + propertyName + "@" + beanType.getCanonicalName());
            }
        }

        public void addField(PropertyField propertyField) {
            if (this.propertyField == null) {
                this.propertyField = propertyField;
            }
        }

        public void addSuperGetter(GetPropertyMethod getMethod) {
            if (this.getMethod == null) {
                this.getMethod = getMethod;
            }
        }

        public void addSuperSetter(SetPropertyMethod setMethod) {
            if (this.setMethod == null) {
                this.setMethod = setMethod;
            }
        }

        public void addSuperField(PropertyField field) {
            if (this.propertyField == null) {
                this.propertyField = field;
            }
        }
    }
}
