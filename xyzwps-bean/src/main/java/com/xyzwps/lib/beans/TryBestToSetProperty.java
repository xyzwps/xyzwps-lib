package com.xyzwps.lib.beans;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;


class TryBestToSetProperty {

    static final TryBestToSetProperty INSTANCE = new TryBestToSetProperty();

    private final ConcurrentMap<Class<?>, Strategy> strategies = new ConcurrentHashMap<>();

    TryBestToSetProperty add(Strategy strategy) {
        Objects.requireNonNull(strategy);
        strategies.put(strategy.propertyType, strategy);
        return this;
    }

    public Object convertToSet(Class<?> propertyType, Object value) {
        Objects.requireNonNull(propertyType);
        var strategy = strategies.get(propertyType);
        return strategy == null ? value : strategy.convertToSet(value);
    }

    public TryBestToSetProperty() {
        this
                .add(new Strategy(boolean.class, (value) -> {
                    if (value == null) return false;
                    return value;
                }))
                .add(new Strategy(short.class, (value) -> {
                    if (value == null) return (short) 0;
                    if (value instanceof Number n) return n.shortValue();
                    return value;
                }))
                .add(new Strategy(int.class, (value) -> {
                    if (value == null) return 0;
                    if (value instanceof Number n) return n.intValue();
                    return value;
                }))
                .add(new Strategy(long.class, (value) -> {
                    if (value == null) return 0L;
                    if (value instanceof Number n) return n.longValue();
                    return value;
                }))
                .add(new Strategy(float.class, (value) -> {
                    if (value == null) return 0.0f;
                    if (value instanceof Number n) return n.floatValue();
                    return value;
                }))
                .add(new Strategy(double.class, (value) -> {
                    if (value == null) return 0.0;
                    if (value instanceof Number n) return n.doubleValue();
                    return value;
                }))
                .add(new Strategy(byte.class, (value) -> {
                    if (value == null) return (byte) 0;
                    return value;
                }))
                .add(new Strategy(char.class, (value) -> {
                    if (value == null) return '\u0000';
                    if (value instanceof CharSequence seq && seq.length() == 1) {
                        return seq.charAt(0);
                    }
                    return value;
                }))
                .add(new Strategy(Boolean.class, Function.identity()))
                .add(new Strategy(Short.class, (value) -> {
                    if (value instanceof Number n) return n.shortValue();
                    return value;
                }))
                .add(new Strategy(Integer.class, (value) -> {
                    if (value instanceof Number n) return n.intValue();
                    return value;
                }))
                .add(new Strategy(Long.class, (value) -> {
                    if (value instanceof Number n) return n.longValue();
                    return value;
                }))
                .add(new Strategy(Float.class, (value) -> {
                    if (value instanceof Number n) return n.floatValue();
                    return value;
                }))
                .add(new Strategy(Double.class, (value) -> {
                    if (value instanceof Number n) return n.doubleValue();
                    return value;
                }))
                .add(new Strategy(Byte.class, Function.identity()))
                .add(new Strategy(Character.class, (value) -> {
                    if (value instanceof CharSequence seq && seq.length() == 1) {
                        return seq.charAt(0);
                    }
                    return value;
                }));
    }

    static final class Strategy {
        public final Class<?> propertyType;
        private final Function<Object, Object> convertToSet;

        public Strategy(Class<?> propertyType, Function<Object, Object> convertToSet) {
            this.propertyType = Objects.requireNonNull(propertyType);
            this.convertToSet = Objects.requireNonNull(convertToSet);
        }

        public Object convertToSet(Object value) {
            return this.convertToSet.apply(value);
        }

        @Override
        public int hashCode() {
            return this.propertyType.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;

            if (obj instanceof Strategy s) {
                return this.propertyType.equals(s.propertyType);
            }

            return false;
        }
    }
}
