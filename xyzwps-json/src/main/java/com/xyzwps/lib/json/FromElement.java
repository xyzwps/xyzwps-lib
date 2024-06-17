package com.xyzwps.lib.json;

import com.xyzwps.lib.json.element.*;

import java.lang.reflect.*;

public interface FromElement {
    <T> T fromElement(JsonElement element, Type type);

    void addFromElementConverter(Class<?> type, FromElementConverter<?, ?> converter);

    void addFromKeyConverter(Class<?> type, FromKeyConverter<?> converter);
}
