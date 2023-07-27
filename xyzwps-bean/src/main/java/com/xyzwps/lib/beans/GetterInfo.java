package com.xyzwps.lib.beans;

import java.util.Objects;

import static com.xyzwps.lib.beans.PropertyMethod.*;

public final class GetterInfo {
    private final boolean readable;
    private final boolean useMethod;
    private final GetPropertyMethod getMethod;
    private final SpringField springField;

    private GetterInfo(GetPropertyMethod getMethod, SpringField springField, boolean readable, boolean useMethod) {
        this.readable = readable;
        this.useMethod = useMethod;
        if (useMethod) {
            this.getMethod = Objects.requireNonNull(getMethod);
            this.springField = springField;
        } else {
            this.getMethod = getMethod;
            this.springField = Objects.requireNonNull(springField);
        }
    }


    public GetResult get(Object object) {
        if (!readable) {
            return GetResult.NOT_READABLE;
        }
        return useMethod ? getMethod.getValue(object) : springField.getValue(object);
    }

    public boolean isReadable() {
        return readable;
    }

    public SpringField getField() {
        return springField;
    }

    public GetPropertyMethod getGetMethod() {
        return getMethod;
    }

    public static GetterInfo create(GetPropertyMethod getMethod, SpringField springField) {
        if (getMethod != null && getMethod.accessLevel() == AccessLevel.PUBLIC) {
            return new GetterInfo(getMethod, springField, true, true);
        }
        if (springField != null && springField.getAccessLevel() == AccessLevel.PUBLIC) {
            return new GetterInfo(getMethod, springField, true, false);
        }
        return new GetterInfo(getMethod, springField, false, false);
    }
}