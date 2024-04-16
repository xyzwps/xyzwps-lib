package com.xyzwps.lib.beans;

import com.xyzwps.lib.beans.ex.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.xyzwps.lib.beans.PropertyMethod.*;

public class PropertyInfo {

    private final String propertyName;
    private final Class<?> beanType;
    private final Class<?> propertyType;
    private final AnnotationsInfo annotations;
    private final PropertyGetter propertyGetter;
    private final PropertySetter propertySetter;
    private final boolean readable;
    private final boolean writable;
    // don‘t support indexed properties

    PropertyInfo(Class<?> beanType, String propertyName, Class<?> propertyType, PropertyGetter propertyGetter, PropertySetter propertySetter) {
        this.beanType = Objects.requireNonNull(beanType);
        this.propertyName = Objects.requireNonNull(propertyName);
        this.propertyType = Objects.requireNonNull(propertyType);

        if (propertyGetter == null && propertySetter == null) {
            throw new IllegalArgumentException("There should be at least one of GetterInfo and SetterInfo.");
        }

        this.propertyGetter = propertyGetter;
        this.propertySetter = propertySetter;
        this.readable = propertyGetter != null && propertyGetter.isReadable();
        this.writable = propertySetter != null && propertySetter.isWritable();
        this.annotations = new AnnotationsInfo(propertyGetter, propertySetter);
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

    private static final Map<Class<?>, Object> primitiveDefaultValues = Map.of(
            boolean.class, false,
            short.class, (short) 0,
            int.class, 0,
            long.class, 0L,
            float.class, 0.0f,
            double.class, 0.0,
            byte.class, (byte) 0,
            char.class, (char) 0
    );


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

    public Class<?> getBeanType() {
        return beanType;
    }

    public AnnotationsInfo getAnnotations() {
        return annotations;
    }

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
            return Optional.of(new PropertyInfo(beanType, propertyName, propertyType, getterInfo, setterInfo));
        }


        private Class<?> decidePropertyType() {
            Class<?> propertyType = null;
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
