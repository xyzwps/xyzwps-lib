package com.xyzwps.lib.json.mapper;

import java.util.HashMap;
import java.util.Map;

public final class Mappers {

    private final Map<Class<?>, Map<Class<?>, Mapper<?, ?>>> mappers = new HashMap<>();

    public Mappers() {
        this
                .add(new JsonDecimalAndBigDecimal())
                .add(new JsonDecimalAndDouble())
                .add(new JsonDecimalAndFloat())
                .add(new JsonIntegerAndBigDecimal())
                .add(new JsonIntegerAndBigInteger())
                .add(new JsonIntegerAndDouble())
                .add(new JsonIntegerAndFloat())
                .add(new JsonIntegerAndBigInteger())
                .add(new JsonIntegerAndLong())
                .add(new JsonIntegerAndShort())
                .add(new JsonStringAndString())
        ;
    }

    public Mappers add(Mapper<?, ?> mapper) {
        getOrCreateMap(mapper.getValueType()).put(mapper.getElementType(), mapper);
        return this;
    }

    private Map<Class<?>, Mapper<?, ?>> getOrCreateMap(Class<?> valueType) {
        var map = mappers.get(valueType);
        if (map == null) {
            Map<Class<?>, Mapper<?, ?>> newMap = new HashMap<>();
            mappers.put(valueType, newMap);
            return newMap;
        } else {
            return map;
        }
    }
}
