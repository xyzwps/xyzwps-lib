package com.xyzwps.lib.json;

import com.xyzwps.lib.json.element.JsonElement;

public interface FromElementConverter<E extends JsonElement, T> {
    T convert(E e);
}
