package com.xyzwps.lib.json.mapper.builtin;

import com.xyzwps.lib.json.element.JsonInteger;
import com.xyzwps.lib.json.element.JsonString;
import com.xyzwps.lib.json.mapper.BaseMapper;
import com.xyzwps.lib.json.mapper.Mapper;
import com.xyzwps.lib.json.mapper.TheMapper;

import java.math.BigInteger;

public final class ShortMappers {

    public static final Mapper<Short, JsonInteger> JSON_INTEGER = new AndInteger();

    public static final Mapper<Short, JsonString> JSON_STRING = new AndString();

    private static class AndInteger extends BaseMapper<Short, JsonInteger> {
        AndInteger() {
            super(JsonInteger.class, Short.class);
        }

        @Override
        public Short toValue(JsonInteger element, TheMapper m) {
            return element.value().shortValue();
        }

        @Override
        public JsonInteger toElement(Short s, TheMapper m) {
            return new JsonInteger(BigInteger.valueOf(s));
        }
    }

    private static class AndString extends BaseMapper<Short, JsonString> {

        protected AndString() {
            super(JsonString.class, Short.class);
        }


        @Override
        public Short toValue(JsonString element, TheMapper m) {
            return Short.valueOf(element.value());
        }

        @Override
        public JsonString toElement(Short s, TheMapper m) {
            return new JsonString(s.toString());
        }
    }
}
