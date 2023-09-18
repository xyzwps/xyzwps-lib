package com.xyzwps.lib.beans;

import java.util.Objects;

public class NoSuchPropertyException extends RuntimeException {

    private final PropertyInfo property;

    public NoSuchPropertyException(PropertyInfo property) {
        super(""); // TODO: 添加描述
        this.property = Objects.requireNonNull(property);
    }
}
