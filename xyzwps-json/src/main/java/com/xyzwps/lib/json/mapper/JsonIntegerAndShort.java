package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonInteger;

public class JsonIntegerAndShort extends BaseMapper<Short, JsonInteger> {

    protected JsonIntegerAndShort() {
        super(JsonInteger.class, Short.class);
    }

    @Override
    public Short toValue(JsonInteger element) {
        return element.value().shortValue();
    }
}
