package com.xyzwps.lib.beans;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

class TryBestToGetProperty {

    static final TryBestToGetProperty INSTANCE = new TryBestToGetProperty();

    private final ConcurrentMap<Type, ConcurrentMap<Type, Strategy>> strategies = new ConcurrentHashMap<>();

    TryBestToGetProperty() {
        this.add(new Strategy(Boolean.class, boolean.class, (value) -> {
                    if (value == null) return false;
                    if (value instanceof Boolean b) return b;
                    return value;
                }))
                .add(new Strategy(Short.class, short.class, (value) -> {
                    if (value == null) return (short) 0;
                    if (value instanceof Number n) return n.shortValue();
                    return value;
                }))
                .add(new Strategy(Integer.class, int.class, (value) -> {
                    if (value == null) return 0;
                    if (value instanceof Number n) return n.intValue();
                    return value;
                }))
                .add(new Strategy(Long.class, long.class, (value) -> {
                    if (value == null) return 0L;
                    if (value instanceof Number n) return n.longValue();
                    return value;
                }))
                .add(new Strategy(Float.class, float.class, (value) -> {
                    if (value == null) return 0.0f;
                    if (value instanceof Number n) return n.floatValue();
                    return value;
                }))
                .add(new Strategy(Double.class, double.class, (value) -> {
                    if (value == null) return 0.0;
                    if (value instanceof Number n) return n.doubleValue();
                    return value;
                }))
                .add(new Strategy(Byte.class, byte.class, (Object value) -> {
                    if (value == null) return (byte) 0;
                    if (value instanceof Byte b) return b;
                    return value;
                }))
                .add(new Strategy(Character.class, char.class, (Object value) -> {
                    if (value == null) return (char) 0;
                    if (value instanceof Character c) return c;
                    if (value instanceof CharSequence s && s.length() == 1) return s.charAt(0);
                    return value;
                }));
    }

    TryBestToGetProperty add(Strategy strategy) {
        Objects.requireNonNull(strategy);
        var propType = strategy.propertyType;
        strategies.computeIfAbsent(propType, (ignored) -> new ConcurrentHashMap<>());
        strategies.get(propType).put(strategy.returnType, strategy);
        return this;
    }

    public Object tryToGet(Type propertyType, Type returnType, Object value) {
        Objects.requireNonNull(propertyType);
        Objects.requireNonNull(returnType);

        var map = strategies.get(propertyType);
        if (map == null || map.isEmpty()) {
            return value;
        }

        var strategry = map.get(returnType);
        return strategry == null ? value : strategry.get(value);
    }

    static class Strategy {

        public final Class<?> propertyType;

        public final Class<?> returnType;

        private final Function<Object, Object> getFn;

        Strategy(Class<?> propertyType, Class<?> returnType, Function<Object, Object> getFn) {
            this.propertyType = Objects.requireNonNull(propertyType);
            this.returnType = Objects.requireNonNull(returnType);
            this.getFn = Objects.requireNonNull(getFn);
        }

        public Object get(Object value) {
            return this.getFn.apply(value);
        }

        @Override
        public final int hashCode() {
            return Objects.hash(this.propertyType, this.returnType);
        }

        @Override
        public final boolean equals(Object obj) {
            if (obj == null) return false;

            if (obj instanceof Strategy s) {
                return this.returnType.equals(s.returnType) && this.propertyType.equals(s.propertyType);
            }

            return false;
        }
    }
}
