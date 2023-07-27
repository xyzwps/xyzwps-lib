package com.xyzwps.lib.beans;

import java.util.Objects;

import static com.xyzwps.lib.beans.PropertyMethod.*;

public final class SetterInfo {
    private final boolean writable;
    private final boolean useMethod;
    private final SetPropertyMethod setMethod;
    private final SpringField springField;

    private SetterInfo(SetPropertyMethod setMethod, SpringField springField, boolean writable, boolean useMethod) {
        this.writable = writable;
        this.useMethod = useMethod;
        if (useMethod) {
            this.setMethod = Objects.requireNonNull(setMethod);
            this.springField = springField;
        } else {
            this.setMethod = setMethod;
            this.springField = Objects.requireNonNull(springField);
        }
    }

    public SetResult set(Object object, Object value) {
        if (!writable) {
            return SetResult.NOT_WRITABLE;
        }
        return useMethod ? setMethod.setValue(object, value) : springField.setValue(object, value);
    }

    public boolean isWritable() {
        return writable;
    }

    public SpringField getField() {
        return springField;
    }

    public SetPropertyMethod getSetMethod() {
        return setMethod;
    }

    public static SetterInfo create(SetPropertyMethod setMethod, SpringField springField) {
        if (setMethod != null && setMethod.accessLevel() == AccessLevel.PUBLIC) {
            return new SetterInfo(setMethod, springField, true, true);
        }
        if (springField != null && springField.getAccessLevel() == AccessLevel.PUBLIC && !springField.isFinal()) {
            return new SetterInfo(setMethod, springField, true, false);
        }
        return new SetterInfo(setMethod, springField, false, false);
    }
}