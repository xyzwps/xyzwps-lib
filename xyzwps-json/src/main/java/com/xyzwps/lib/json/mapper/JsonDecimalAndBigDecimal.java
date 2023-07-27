package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonDecimal;

import java.math.BigDecimal;

public class JsonDecimalAndBigDecimal extends BaseMapper<BigDecimal, JsonDecimal> {

    protected JsonDecimalAndBigDecimal() {
        super(JsonDecimal.class, BigDecimal.class);
    }

    @Override
    public BigDecimal toValue(JsonDecimal element) {
        return element.value();
    }
}