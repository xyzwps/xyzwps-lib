package com.xyzwps.lib.beans.ex;

import com.xyzwps.lib.beans.PropertyInfo;

import java.util.Objects;

public class PropertyIsNotReadableException extends RuntimeException {
    private final PropertyInfo property;

    public PropertyIsNotReadableException(PropertyInfo property) {
        super(""); // TODO: 添加描述
        this.property = Objects.requireNonNull(property);
    }
}
