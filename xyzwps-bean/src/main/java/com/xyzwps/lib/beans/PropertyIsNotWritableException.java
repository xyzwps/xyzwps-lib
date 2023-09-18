package com.xyzwps.lib.beans;

import java.util.Objects;

public class PropertyIsNotWritableException extends RuntimeException {
    private final PropertyInfo property;

    public PropertyIsNotWritableException(PropertyInfo property) {
        super(""); // TODO: 添加描述
        this.property = Objects.requireNonNull(property);
    }
}
