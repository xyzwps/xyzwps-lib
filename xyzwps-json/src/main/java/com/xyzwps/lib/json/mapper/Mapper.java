package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonElement;

public interface Mapper<V, E extends JsonElement> {
    V toValue(E element, TheMapper m);

    E toElement(V v, TheMapper m);

    Class<V> getValueType();

    Class<E> getElementType();
}
