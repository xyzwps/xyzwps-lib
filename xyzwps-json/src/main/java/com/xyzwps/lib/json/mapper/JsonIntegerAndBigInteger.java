package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonInteger;

import java.math.BigInteger;

public class JsonIntegerAndBigInteger extends BaseMapper<BigInteger, JsonInteger> {

    protected JsonIntegerAndBigInteger() {
        super(JsonInteger.class, BigInteger.class);
    }

    @Override
    public BigInteger toValue(JsonInteger element) {
        return element.value();
    }
}
