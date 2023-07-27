package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonString;

public class JsonStringAndString extends BaseMapper<String, JsonString> {

    public JsonStringAndString() {
        super(JsonString.class, String.class);
    }

    @Override
    public String toValue(JsonString element) {
        return element.value();
    }
}
