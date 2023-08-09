package com.xyzwps.lib.json.mapper.builtin;

import com.xyzwps.lib.json.element.JsonBoolean;
import com.xyzwps.lib.json.element.JsonDecimal;
import com.xyzwps.lib.json.element.JsonInteger;
import com.xyzwps.lib.json.element.JsonString;
import com.xyzwps.lib.json.mapper.BaseMapper;
import com.xyzwps.lib.json.mapper.Mapper;
import com.xyzwps.lib.json.mapper.TheMapper;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class StringMappers {

    public static final Mapper<String, JsonDecimal> JSON_DECIMAL = new AndDecimal();

    public static final Mapper<String, JsonInteger> JSON_INTEGER = new AndInteger();

    public static final Mapper<String, JsonBoolean> JSON_BOOLEAN = new AndBoolean();

    public static final Mapper<String, JsonString> JSON_STRING = new AndString();

    private static class AndString extends BaseMapper<String, JsonString> {

        AndString() {
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

    private static class AndBoolean extends BaseMapper<String, JsonBoolean> {

        AndBoolean() {
            super(JsonBoolean.class, String.class);
        }

        @Override
        public String toValue(JsonBoolean element, TheMapper m) {
            return Boolean.toString(element.value);
        }

        @Override
        public JsonBoolean toElement(String s, TheMapper m) {
            return JsonBoolean.of(Boolean.parseBoolean(s));
        }
    }

    private static class AndInteger extends BaseMapper<String, JsonInteger> {

        AndInteger() {
            super(JsonInteger.class, String.class);
        }

        @Override
        public String toValue(JsonInteger element, TheMapper m) {
            return element.value().toString();
        }

        @Override
        public JsonInteger toElement(String s, TheMapper m) {
            return new JsonInteger(new BigInteger(s));
        }
    }

    private static class AndDecimal extends BaseMapper<String, JsonDecimal> {

        AndDecimal() {
            super(JsonDecimal.class, String.class);
        }

        @Override
        public String toValue(JsonDecimal element, TheMapper m) {
            return element.value().toString();
        }

        @Override
        public JsonDecimal toElement(String s, TheMapper m) {
            return new JsonDecimal(new BigDecimal(s));
        }
    }
}
