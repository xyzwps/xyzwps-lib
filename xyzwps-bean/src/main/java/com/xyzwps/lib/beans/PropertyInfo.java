package com.xyzwps.lib.beans;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.xyzwps.lib.beans.PropertyMethod.*;

public class PropertyInfo {

    private final String propertyName;
    private final Class<?> beanType;
    private final Class<?> propertyType;
    private final AnnotationsInfo annotations;
    private final GetterInfo getterInfo;
    private final SetterInfo setterInfo;
    private final boolean readable;
    private final boolean writable;
    // TODO: support indexed properties

    public PropertyInfo(Class<?> beanType, String propertyName, Class<?> propertyType, GetterInfo getterInfo, SetterInfo setterInfo) {
        this.beanType = Objects.requireNonNull(beanType);
        this.propertyName = Objects.requireNonNull(propertyName);
        this.propertyType = Objects.requireNonNull(propertyType);

        if (getterInfo == null && setterInfo == null) {
            throw new IllegalArgumentException("There should be at least one of GetterInfo and SetterInfo.");
        }

        this.getterInfo = getterInfo;
        this.setterInfo = setterInfo;
        this.readable = getterInfo != null && getterInfo.isReadable();
        this.writable = setterInfo != null && setterInfo.isWritable();
        this.annotations = new AnnotationsInfo(getterInfo, setterInfo);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    public boolean isReadable() {
        return readable;
    }

    public boolean isWritable() {
        return writable;
    }

    public GetResult getProperty(Object object) {
        return getterInfo.get(object);
    }

    public <T> T getPropertyOrThrow(Object object, Class<T> tClass) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(tClass);
        return switch (getterInfo.get(object)) {
            case GetResult.NoSuchProperty ignored -> throw new NoSuchPropertyException(this);
            case GetResult.Failed failed -> throw new UnhandledBeanException(failed.cause());
            case GetResult.NotReadable ignored -> throw new PropertyIsNotReadableException(this);
            case GetResult.Ok ok -> {
                var value = ok.value();
                if (fixType(tClass).isAssignableFrom(value.getClass())) {
                    yield (T) value;
                } else {
                    throw new PropertyCaseException(this, tClass);
                }
            }
        };
    }

    private static Class<?> fixType(Class<?> t) {
        return primitiveToWrapper.getOrDefault(t, t);
    }

    private static final Map<Class<?>, Class<?>> primitiveToWrapper = Map.of(
            boolean.class, Boolean.class,
            short.class, Short.class,
            int.class, Integer.class,
            long.class, Long.class,
            float.class, Float.class,
            double.class, Double.class,
            byte.class, Byte.class,
            char.class, Character.class
    );

    public SetResult setProperty(Object object, Object value) {
        // TODO: 处理 short 等原生类型
        return setterInfo.set(object, value);
    }

    public void setPropertyOrThrow(Object object, Object value) {
        Objects.requireNonNull(object);
        switch (setterInfo.set(object, value)) {
            case SetResult.Failed failed -> throw new UnhandledBeanException(failed.cause());
            case SetResult.NoSuchProperty ignored -> throw new NoSuchPropertyException(this);
            case SetResult.NotWritable ignored -> throw new PropertyIsNotWritableException(this);
            case SetResult.Ok ignored -> {
            }
        }
    }

    public AnnotationsInfo getAnnotations() {
        return annotations;
    }

    public static class Holder {

        private final String propertyName;
        private final Class<?> beanType;
        private SetPropertyMethod setMethod;
        private GetPropertyMethod getMethod;
        private SpringField springField;


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
            var getterInfo = GetterInfo.create(getMethod, springField);
            var setterInfo = SetterInfo.create(setMethod, springField);
            return Optional.of(new PropertyInfo(beanType, propertyName, propertyType, getterInfo, setterInfo));
        }


        private Class<?> decidePropertyType() {
            Class<?> propertyType = null;
            if (springField != null) {
                propertyType = springField.getFieldType();
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

        public void addField(SpringField springField) {
            if (this.springField == null) {
                this.springField = springField;
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

        public void addSuperField(SpringField field) {
            if (this.springField == null) {
                this.springField = field;
            }
        }
    }
}
