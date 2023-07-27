package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonInteger;

public class JsonIntegerAndDouble extends BaseMapper<Double, JsonInteger> {

    protected JsonIntegerAndDouble() {
        super(JsonInteger.class, Double.class);
    }

    @Override
    public Double toValue(JsonInteger element) {
        return element.value().doubleValue();
    }
}
