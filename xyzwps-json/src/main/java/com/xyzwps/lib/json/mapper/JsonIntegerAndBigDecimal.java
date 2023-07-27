package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonInteger;

import java.math.BigDecimal;

public class JsonIntegerAndBigDecimal extends BaseMapper<BigDecimal, JsonInteger> {

    protected JsonIntegerAndBigDecimal() {
        super(JsonInteger.class, BigDecimal.class);
    }

    @Override
    public BigDecimal toValue(JsonInteger element) {
        return new BigDecimal(element.value());
    }
}
