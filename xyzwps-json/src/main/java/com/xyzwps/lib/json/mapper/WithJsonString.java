package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonString;

class WithJsonString {
    public static class AndString extends BaseMapper<String, JsonString> {

        public AndString() {
            super(JsonString.class, String.class);
        }

        @Override
        public String toValue(JsonString element) {
            return element.value();
        }
    }
}
