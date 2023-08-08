package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonElement;

import java.util.Objects;

public class NoSuchMapperException extends RuntimeException {

    public final Class<? extends JsonElement> jsonElementType;

    public final Class<?> valueType;

    public NoSuchMapperException(Class<? extends JsonElement> jsonElementType, Class<?> valueType) {
        super("No such mapper.");
        this.jsonElementType = Objects.requireNonNull(jsonElementType);
        this.valueType = Objects.requireNonNull(valueType);
    }
}
