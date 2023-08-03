package com.xyzwps.lib.json.mapper;

import java.util.HashMap;
import java.util.Map;

public final class Mappers {

    private final Map<Class<?>, Map<Class<?>, Mapper<?, ?>>> mappers = new HashMap<>();

    public Mappers() {
        this.placeholder()
                .add(new WithJsonDecimal.AndBigDecimal())
                .add(new WithJsonDecimal.AndDouble())
                .add(new WithJsonDecimal.AndFloat())
                .add(new WithJsonInteger.AndBigDecimal())
                .add(new WithJsonInteger.AndBigInteger())
                .add(new WithJsonInteger.AndDouble())
                .add(new WithJsonInteger.AndFloat())
                .add(new WithJsonInteger.AndBigInteger())
                .add(new WithJsonInteger.AndLong())
                .add(new WithJsonInteger.AndShort())
                .add(new WithJsonString.AndString())
        ;
    }

    public Mappers add(Mapper<?, ?> mapper) {
        getOrCreateMap(mapper.getValueType()).put(mapper.getElementType(), mapper);
        return this;
    }

    private Mappers placeholder() {
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
