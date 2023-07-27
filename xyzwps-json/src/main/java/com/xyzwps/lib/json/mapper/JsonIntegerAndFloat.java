package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonInteger;

public class JsonIntegerAndFloat extends BaseMapper<Float, JsonInteger> {

    protected JsonIntegerAndFloat() {
        super(JsonInteger.class, Float.class);
    }

    @Override
    public Float toValue(JsonInteger element) {
        return element.value().floatValue();
    }
}
