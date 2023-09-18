package com.xyzwps.lib.beans;

import java.util.Objects;

public class PropertyCaseException extends RuntimeException {

    private final PropertyInfo property;
    private final Class<?> castTo;

    public PropertyCaseException(PropertyInfo property, Class<?> castTo) {
        super(""); // TODO: 添加描述
        this.property = Objects.requireNonNull(property);
        this.castTo = Objects.requireNonNull(castTo);
    }
}
