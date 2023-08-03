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
        public BigDecimal toValue(JsonInteger element) {
            return new BigDecimal(element.value());
        }
    }

    public static class AndBigInteger extends BaseMapper<BigInteger, JsonInteger> {

        protected AndBigInteger() {
            super(JsonInteger.class, BigInteger.class);
        }

        @Override
        public BigInteger toValue(JsonInteger element) {
            return element.value();
        }
    }

    public static class AndDouble extends BaseMapper<Double, JsonInteger> {

        protected AndDouble() {
            super(JsonInteger.class, Double.class);
        }

        @Override
        public Double toValue(JsonInteger element) {
            return element.value().doubleValue();
        }
    }

    public static class AndFloat extends BaseMapper<Float, JsonInteger> {

        protected AndFloat() {
            super(JsonInteger.class, Float.class);
        }

        @Override
        public Float toValue(JsonInteger element) {
            return element.value().floatValue();
        }
    }

    public static class AndInteger extends BaseMapper<Integer, JsonInteger> {

        protected AndInteger() {
            super(JsonInteger.class, Integer.class);
        }

        @Override
        public Integer toValue(JsonInteger element) {
            return element.value().intValue();
        }
    }

    public static class AndLong extends BaseMapper<Long, JsonInteger> {

        protected AndLong() {
            super(JsonInteger.class, Long.class);
        }

        @Override
        public Long toValue(JsonInteger element) {
            return element.value().longValue();
        }
    }

    public static class AndShort extends BaseMapper<Short, JsonInteger> {

        protected AndShort() {
            super(JsonInteger.class, Short.class);
        }

        @Override
        public Short toValue(JsonInteger element) {
            return element.value().shortValue();
        }
    }
}
