package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonInteger;

public class JsonIntegerAndLong extends BaseMapper<Long, JsonInteger> {

    protected JsonIntegerAndLong() {
        super(JsonInteger.class, Long.class);
    }

    @Override
    public Long toValue(JsonInteger element) {
        return element.value().longValue();
    }
}
