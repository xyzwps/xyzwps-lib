package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonDecimal;

import java.math.BigDecimal;

class WithJsonDecimal {

    public static class AndBigDecimal extends BaseMapper<BigDecimal, JsonDecimal> {

        protected AndBigDecimal() {
            super(JsonDecimal.class, BigDecimal.class);
        }

        @Override
        public BigDecimal toValue(JsonDecimal element) {
            return element.value();
        }
    }

    public static class AndDouble extends BaseMapper<Double, JsonDecimal> {

        protected AndDouble() {
            super(JsonDecimal.class, Double.class);
        }

        @Override
        public Double toValue(JsonDecimal element) {
            return element.value().doubleValue();
        }
    }

    public static class AndFloat extends BaseMapper<Float, JsonDecimal> {

        protected AndFloat() {
            super(JsonDecimal.class, Float.class);
        }

        @Override
        public Float toValue(JsonDecimal element) {
            return element.value().floatValue();
        }
    }
}
