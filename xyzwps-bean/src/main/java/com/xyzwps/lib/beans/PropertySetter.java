package com.xyzwps.lib.beans;

import java.util.Objects;

import static com.xyzwps.lib.beans.PropertyMethod.*;

public final class PropertySetter {
    private final boolean writable;
    private final boolean useMethod;
    private final SetPropertyMethod setMethod;
    private final PropertyField propertyField;

    private PropertySetter(SetPropertyMethod setMethod, PropertyField propertyField, boolean writable, boolean useMethod) {
        this.writable = writable;
        this.useMethod = useMethod;
        if (useMethod) {
            this.setMethod = Objects.requireNonNull(setMethod);
            this.propertyField = propertyField;
        } else {
            this.setMethod = setMethod;
            this.propertyField = Objects.requireNonNull(propertyField);
        }
    }

    public SetResult set(Object object, Object value) {
        if (!writable) {
            return SetResult.NOT_WRITABLE;
        }
        return useMethod ? setMethod.setValue(object, value) : propertyField.setValue(object, value);
    }

    public boolean isWritable() {
        return writable;
    }

    public PropertyField getField() {
        return propertyField;
    }

    public SetPropertyMethod getSetMethod() {
        return setMethod;
    }

    public static PropertySetter create(SetPropertyMethod setMethod, PropertyField propertyField) {
        if (setMethod != null && setMethod.accessLevel() == AccessLevel.PUBLIC) {
            return new PropertySetter(setMethod, propertyField, true, true);
        }
        if (propertyField != null && propertyField.getAccessLevel() == AccessLevel.PUBLIC && !propertyField.isFinal()) {
            return new PropertySetter(setMethod, propertyField, true, false);
        }
        return new PropertySetter(setMethod, propertyField, false, false);
    }
}