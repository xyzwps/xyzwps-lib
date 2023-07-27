package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonInteger;

public class JsonIntegerAndInteger extends BaseMapper<Integer, JsonInteger> {

    protected JsonIntegerAndInteger() {
        super(JsonInteger.class, Integer.class);
    }

    @Override
    public Integer toValue(JsonInteger element) {
        return element.value().intValue();
    }
}
