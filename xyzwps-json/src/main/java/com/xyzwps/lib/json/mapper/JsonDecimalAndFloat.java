package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonDecimal;

public class JsonDecimalAndFloat extends BaseMapper<Float, JsonDecimal> {

    protected JsonDecimalAndFloat() {
        super(JsonDecimal.class, Float.class);
    }

    @Override
    public Float toValue(JsonDecimal element) {
        return element.value().floatValue();
    }
}