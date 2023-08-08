package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonString;

class WithJsonString {
    public static class AndString extends BaseMapper<String, JsonString> {

        public AndString() {
            super(JsonString.class, String.class);
        }

        @Override
        public String toValue(JsonString element, TheMapper m) {
            return element.value();
        }

        @Override
        public JsonString toElement(String s, TheMapper m) {
            return new JsonString(s);
        }
    }
}
