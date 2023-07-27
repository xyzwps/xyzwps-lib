package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonDecimal;

public class JsonDecimalAndDouble extends BaseMapper<Double, JsonDecimal> {

    protected JsonDecimalAndDouble() {
        super(JsonDecimal.class, Double.class);
    }

    @Override
    public Double toValue(JsonDecimal element) {
        return element.value().doubleValue();
    }
}