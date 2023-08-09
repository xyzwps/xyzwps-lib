package com.xyzwps.lib.json.mapper.builtin;

import com.xyzwps.lib.json.element.JsonInteger;
import com.xyzwps.lib.json.element.JsonString;
import com.xyzwps.lib.json.mapper.BaseMapper;
import com.xyzwps.lib.json.mapper.Mapper;
import com.xyzwps.lib.json.mapper.TheMapper;

import java.math.BigInteger;

public final class LongMappers {

    public static final Mapper<Long, JsonInteger> JSON_INTEGER = new AndInteger();

    public static final Mapper<Long, JsonString> JSON_STRING = new AndString();

    private static class AndInteger extends BaseMapper<Long, JsonInteger> {

        AndInteger() {
            super(JsonInteger.class, Long.class);
        }

        @Override
        public Long toValue(JsonInteger element, TheMapper m) {
            return element.value().longValue();
        }

        @Override
        public JsonInteger toElement(Long a, TheMapper m) {
            return new JsonInteger(BigInteger.valueOf(a));
        }
    }

    private static class AndString extends BaseMapper<Long, JsonString> {

        AndString() {
            super(JsonString.class, Long.class);
        }

        @Override
        public Long toValue(JsonString element, TheMapper m) {
            return Long.parseLong(element.value());
        }

        @Override
        public JsonString toElement(Long a, TheMapper m) {
            return new JsonString(a.toString());
        }
    }
}
