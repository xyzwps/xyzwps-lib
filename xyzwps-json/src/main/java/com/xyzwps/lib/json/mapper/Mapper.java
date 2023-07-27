package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonElement;

public interface Mapper<V, E extends JsonElement> {
    V toValue(E element);

    Class<V> getValueType();

    Class<E> getElementType();
}
