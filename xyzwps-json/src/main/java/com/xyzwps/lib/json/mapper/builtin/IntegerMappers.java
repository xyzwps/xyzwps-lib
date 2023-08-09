package com.xyzwps.lib.json.mapper.builtin;

import com.xyzwps.lib.json.element.JsonInteger;
import com.xyzwps.lib.json.element.JsonString;
import com.xyzwps.lib.json.mapper.BaseMapper;
import com.xyzwps.lib.json.mapper.Mapper;
import com.xyzwps.lib.json.mapper.TheMapper;

import java.math.BigInteger;

public final class IntegerMappers {

    public static final Mapper<Integer, JsonInteger> JSON_INTEGER = new AndInteger();

    public static final Mapper<Integer, JsonString> JSON_STRING = new AndString();

    private static class AndInteger extends BaseMapper<Integer, JsonInteger> {

        AndInteger() {
            super(JsonInteger.class, Integer.class);
        }

        @Override
        public Integer toValue(JsonInteger element, TheMapper m) {
            return element.value().intValue();
        }

        @Override
        public JsonInteger toElement(Integer v, TheMapper m) {
            return new JsonInteger(BigInteger.valueOf(v.longValue()));
        }
    }

    private static class AndString extends BaseMapper<Integer, JsonString> {

        AndString() {
            super(JsonString.class, Integer.class);
        }

        @Override
        public Integer toValue(JsonString element, TheMapper m) {
            return Integer.parseInt(element.toString());
        }

        @Override
        public JsonString toElement(Integer s, TheMapper m) {
            return new JsonString(s.toString());
        }
    }
}
