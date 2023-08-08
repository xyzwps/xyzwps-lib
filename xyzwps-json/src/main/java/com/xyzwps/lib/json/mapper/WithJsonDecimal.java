package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonDecimal;

import java.math.BigDecimal;

class WithJsonDecimal {

    public static class AndBigDecimal extends BaseMapper<BigDecimal, JsonDecimal> {

        protected AndBigDecimal() {
            super(JsonDecimal.class, BigDecimal.class);
        }

        @Override
        public BigDecimal toValue(JsonDecimal element, TheMapper m) {
            return element.value();
        }

        @Override
        public JsonDecimal toElement(BigDecimal bigDecimal, TheMapper m) {
            return new JsonDecimal(bigDecimal);
        }
    }

    public static class AndDouble extends BaseMapper<Double, JsonDecimal> {

        protected AndDouble() {
            super(JsonDecimal.class, Double.class);
        }

        @Override
        public Double toValue(JsonDecimal element, TheMapper m) {
            return element.value().doubleValue();
        }

        @Override
        public JsonDecimal toElement(Double aDouble, TheMapper m) {
            return new JsonDecimal(BigDecimal.valueOf(aDouble));
        }
    }

    public static class AndFloat extends BaseMapper<Float, JsonDecimal> {

        protected AndFloat() {
            super(JsonDecimal.class, Float.class);
        }

        @Override
        public Float toValue(JsonDecimal element, TheMapper m) {
            return element.value().floatValue();
        }

        @Override
        public JsonDecimal toElement(Float aFloat, TheMapper m) {
            return new JsonDecimal(BigDecimal.valueOf(aFloat));
        }
    }
}
