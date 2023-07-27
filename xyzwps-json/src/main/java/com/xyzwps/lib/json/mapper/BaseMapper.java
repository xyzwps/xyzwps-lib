package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonElement;

import java.util.Objects;

public abstract class BaseMapper<V, E extends JsonElement> implements Mapper<V, E> {

    private final Class<V> vClass;
    private final Class<E> eClass;

    protected BaseMapper(Class<E> eClass, Class<V> vClass) {
        this.eClass = Objects.requireNonNull(eClass);
        this.vClass = Objects.requireNonNull(vClass);
    }

    @Override
    public Class<V> getValueType() {
        return vClass;
    }

    @Override
    public Class<E> getElementType() {
        return eClass;
    }
}
