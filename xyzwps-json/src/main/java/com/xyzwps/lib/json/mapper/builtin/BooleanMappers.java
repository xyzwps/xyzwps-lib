package com.xyzwps.lib.json.mapper.builtin;

import com.xyzwps.lib.json.element.JsonBoolean;
import com.xyzwps.lib.json.element.JsonInteger;
import com.xyzwps.lib.json.mapper.BaseMapper;
import com.xyzwps.lib.json.mapper.Mapper;
import com.xyzwps.lib.json.mapper.TheMapper;

import static java.math.BigInteger.*;

public final class BooleanMappers {

    public static final Mapper<Boolean, JsonBoolean> JSON_BOOLEAN = new AndBoolean();

    public static final Mapper<Boolean, JsonInteger> JSON_INTEGER = new AndInteger();

    private static class AndBoolean extends BaseMapper<Boolean, JsonBoolean> {

        AndBoolean() {
            super(JsonBoolean.class, Boolean.class);
        }

        @Override
        public Boolean toValue(JsonBoolean element, TheMapper m) {
            return element.value;
        }

        @Override
        public JsonBoolean toElement(Boolean v, TheMapper m) {
            return JsonBoolean.of(v);
        }
    }

    private static class AndInteger extends BaseMapper<Boolean, JsonInteger> {

        AndInteger() {
            super(JsonInteger.class, Boolean.class);
        }

        @Override
        public Boolean toValue(JsonInteger element, TheMapper m) {
            return !element.value().equals(ZERO);
        }

        @Override
        public JsonInteger toElement(Boolean v, TheMapper m) {
            return v ? new JsonInteger(ONE) : new JsonInteger(ZERO);
        }
    }

    // TODO: from string

}
