package com.xyzwps.lib.beans;

import java.util.Objects;

import static com.xyzwps.lib.beans.PropertyMethod.*;

public final class PropertyGetter {
    private final boolean readable;
    private final boolean useMethod;
    private final GetPropertyMethod getMethod;
    private final PropertyField propertyField;

    private PropertyGetter(GetPropertyMethod getMethod, PropertyField propertyField, boolean readable, boolean useMethod) {
        this.readable = readable;
        this.useMethod = useMethod;
        if (useMethod) {
            this.getMethod = Objects.requireNonNull(getMethod);
            this.propertyField = propertyField;
        } else {
            this.getMethod = getMethod;
            this.propertyField = Objects.requireNonNull(propertyField);
        }
    }


    public GetResult get(Object object) {
        if (!readable) {
            return GetResult.NOT_READABLE;
        }
        return useMethod ? getMethod.getValue(object) : propertyField.getValue(object);
    }

    public boolean isReadable() {
        return readable;
    }

    public PropertyField getField() {
        return propertyField;
    }

    public GetPropertyMethod getGetMethod() {
        return getMethod;
    }

    public static PropertyGetter create(GetPropertyMethod getMethod, PropertyField propertyField) {
        if (getMethod != null && getMethod.accessLevel() == AccessLevel.PUBLIC) {
            return new PropertyGetter(getMethod, propertyField, true, true);
        }
        if (propertyField != null && propertyField.getAccessLevel() == AccessLevel.PUBLIC) {
            return new PropertyGetter(getMethod, propertyField, true, false);
        }
        return new PropertyGetter(getMethod, propertyField, false, false);
    }
}