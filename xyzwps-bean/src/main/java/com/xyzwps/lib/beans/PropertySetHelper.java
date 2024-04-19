package com.xyzwps.lib.beans;

import com.xyzwps.lib.bedrock.lang.DefaultValues;

import java.lang.reflect.Type;

class PropertySetHelper {

    private static BeanException castError(Object obj, Type propType) {
        return new BeanException("Cannot cast " + obj.getClass().getTypeName() + " to " + propType.getTypeName());
    }

    static Object toSettableValue(Object obj, Type propType) {
        if (obj == null) {
            return DefaultValues.get(propType);
        }

        if (propType == byte.class || propType == Byte.class) {
            if (obj instanceof Byte) return obj;
            throw castError(obj, propType);
        }

        if (propType == short.class || propType == Short.class) {
            if (obj instanceof Byte b) return b.shortValue();
            if (obj instanceof Short s) return s;
            throw castError(obj, propType);
        }

        if (propType == int.class || propType == Integer.class) {
            return switch (obj) {
                case Byte b -> b.intValue();
                case Short s -> s.intValue();
                case Character c -> (int) c;
                case Integer i -> i;
                default -> throw castError(obj, propType);
            };
        }

        if (propType == long.class || propType == Long.class) {
            return switch (obj) {
                case Byte b -> b.longValue();
                case Short s -> s.longValue();
                case Character c -> (long) c;
                case Integer i -> i.longValue();
                case Long l -> l;
                default -> throw castError(obj, propType);
            };
        }

        if (propType == float.class || propType == Float.class) {
            return switch (obj) {
                case Byte b -> b.floatValue();
                case Short s -> s.floatValue();
                case Character c -> (float) c;
                case Integer i -> i.floatValue();
                case Long l -> l.floatValue();
                case Float f -> f;
                default -> throw castError(obj, propType);
            };
        }

        if (propType == double.class || propType == Double.class) {
            return switch (obj) {
                case Byte b -> b.doubleValue();
                case Short s -> s.doubleValue();
                case Character c -> (double) c;
                case Integer i -> i.doubleValue();
                case Long l -> l.doubleValue();
                case Float f -> f.doubleValue();
                case Double d -> d;
                default -> throw castError(obj, propType);
            };
        }

        if (propType == boolean.class || propType == Boolean.class) {
            if (obj instanceof Boolean b) return b;
            throw castError(obj, propType);
        }

        if (propType == char.class || propType == Character.class) {
            if (obj instanceof Character c) return c;
            if (obj instanceof String s && s.length() == 1) return s.charAt(0);
            throw castError(obj, propType);
        }

        return obj;
    }
}
