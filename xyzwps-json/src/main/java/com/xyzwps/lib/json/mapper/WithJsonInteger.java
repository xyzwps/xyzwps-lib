package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonInteger;

import java.math.BigDecimal;
import java.math.BigInteger;

class WithJsonInteger {

    public static class AndBigDecimal extends BaseMapper<BigDecimal, JsonInteger> {

        protected AndBigDecimal() {
            super(JsonInteger.class, BigDecimal.class);
        }

        @Override
        public BigDecimal toValue(JsonInteger element, TheMapper m) {
            return new BigDecimal(element.value());
        }

        @Override
        public JsonInteger toElement(BigDecimal bigDecimal, TheMapper m) {
            return new JsonInteger(bigDecimal.toBigInteger());
        }
    }

    public static class AndBigInteger extends BaseMapper<BigInteger, JsonInteger> {

        protected AndBigInteger() {
            super(JsonInteger.class, BigInteger.class);
        }

        @Override
        public BigInteger toValue(JsonInteger element, TheMapper m) {
            return element.value();
        }

        @Override
        public JsonInteger toElement(BigInteger bigInteger, TheMapper m) {
            return new JsonInteger(bigInteger);
        }
    }

    public static class AndDouble extends BaseMapper<Double, JsonInteger> {

        protected AndDouble() {
            super(JsonInteger.class, Double.class);
        }

        @Override
        public Double toValue(JsonInteger element, TheMapper m) {
            return element.value().doubleValue();
        }

        @Override
        public JsonInteger toElement(Double aDouble, TheMapper m) {
            return new JsonInteger(BigInteger.valueOf(aDouble.longValue()));
        }
    }

    public static class AndFloat extends BaseMapper<Float, JsonInteger> {

        protected AndFloat() {
            super(JsonInteger.class, Float.class);
        }

        @Override
        public Float toValue(JsonInteger element, TheMapper m) {
            return element.value().floatValue();
        }

        @Override
        public JsonInteger toElement(Float aFloat, TheMapper m) {
            return new JsonInteger(BigInteger.valueOf(aFloat.longValue()));
        }
    }

    public static class AndInteger extends BaseMapper<Integer, JsonInteger> {

        protected AndInteger() {
            super(JsonInteger.class, Integer.class);
        }

        @Override
        public Integer toValue(JsonInteger element, TheMapper m) {
            return element.value().intValue();
        }

        @Override
        public JsonInteger toElement(Integer integer, TheMapper m) {
            return new JsonInteger(BigInteger.valueOf(integer));
        }
    }

    public static class AndLong extends BaseMapper<Long, JsonInteger> {

        protected AndLong() {
            super(JsonInteger.class, Long.class);
        }

        @Override
        public Long toValue(JsonInteger element, TheMapper m) {
            return element.value().longValue();
        }

        @Override
        public JsonInteger toElement(Long aLong, TheMapper m) {
            return new JsonInteger(BigInteger.valueOf(aLong));
        }
    }

    public static class AndShort extends BaseMapper<Short, JsonInteger> {

        protected AndShort() {
            super(JsonInteger.class, Short.class);
        }

        @Override
        public Short toValue(JsonInteger element, TheMapper m) {
            return element.value().shortValue();
        }

        @Override
        public JsonInteger toElement(Short aShort, TheMapper m) {
            return new JsonInteger(BigInteger.valueOf(aShort));
        }
    }
}
