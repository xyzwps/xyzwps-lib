package com.xyzwps.lib.json;

import com.xyzwps.lib.beans.BeanUtils;
import com.xyzwps.lib.json.element.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class ToElement {

    private final ConcurrentHashMap<Class<?>, ToElementConverter<?>> toElementTable;
    private final ConcurrentHashMap<Class<?>, ToKeyConverter<?>> toKeyTable;

    private ToElement() {
        this.toElementTable = new ConcurrentHashMap<>();
        this.toKeyTable = new ConcurrentHashMap<>();
    }

    public void addToElementConverter(Class<?> type, ToElementConverter<?> converter) {
        toElementTable.put(Objects.requireNonNull(type), Objects.requireNonNull(converter));
    }

    public void addToKeyConverter(Class<?> type, ToKeyConverter<?> converter) {
        toKeyTable.put(Objects.requireNonNull(type), Objects.requireNonNull(converter));
    }

    public ToElementConverter<?> getToElementConverter(Class<?> type) {
        return toElementTable.get(Objects.requireNonNull(type));
    }

    public ToKeyConverter<?> getToKeyConverter(Class<?> type) {
        return toKeyTable.get(Objects.requireNonNull(type));
    }

    public static ToElement createDefault() {
        var r = new ToElement();

        r.addToElementConverter(BigDecimal.class, (BigDecimal b) -> new JsonDecimal(b));
        r.addToElementConverter(BigInteger.class, (BigInteger i) -> new JsonInteger(i));
        r.addToElementConverter(Boolean.class, (Boolean b) -> JsonBoolean.of(b));
        r.addToElementConverter(Character.class, (Character c) -> new JsonString(c.toString()));
        r.addToElementConverter(Double.class, (Double d) -> new JsonDecimal(BigDecimal.valueOf(d)));
        r.addToElementConverter(Float.class, (Float f) -> new JsonDecimal(BigDecimal.valueOf(f)));
        r.addToElementConverter(Integer.class, (Integer i) -> new JsonInteger(BigInteger.valueOf(i)));
        r.addToElementConverter(Long.class, (Long l) -> new JsonInteger(BigInteger.valueOf(l)));
        r.addToElementConverter(Short.class, (Short s) -> new JsonInteger(BigInteger.valueOf(s)));
        r.addToElementConverter(String.class, (String s) -> new JsonString(s));

        r.addToKeyConverter(BigInteger.class, (BigInteger i) -> i.toString());
        r.addToKeyConverter(Character.class, (Character c) -> c.toString());
        r.addToKeyConverter(Integer.class, (Integer i) -> i.toString());
        r.addToKeyConverter(Long.class, (Long l) -> l.toString());
        r.addToKeyConverter(Short.class, (Short s) -> s.toString());
        r.addToKeyConverter(String.class, (String s) -> s);

        return r;
    }


    public JsonElement toElement(Object object) {
        if (object == null) {
            return JsonNull.INSTANCE;
        }

        ToElementConverter converter = this.getToElementConverter(object.getClass());
        if (converter != null) {
            return converter.convert(object);
        }

        if (object.getClass().isEnum()) {
            return new JsonString(((Enum<?>) object).name());
        }

        if (object.getClass().isArray()) {
            return fromArray(object);
        }

        if (object instanceof Iterable<?> itr) {
            return fromIterable(itr);
        }

        if (object instanceof Map<?, ?> map) {
            return fromMap(map);
        }

        return fromMap(BeanUtils.getProperties(object));
    }

    private String toKey(Object key) {
        if (key == null) return "null";

        ToKeyConverter converter = this.getToKeyConverter(key.getClass());
        if (converter != null) {
            return converter.convert(key);
        }

        if (key.getClass().isEnum()) {
            return ((Enum<?>) key).name();
        }

        throw new JsonException("Unsupported json key type: " + key.getClass().getCanonicalName());
    }


    private JsonElement fromArray(Object array) {
        var ct = array.getClass().getComponentType();
        var jsonArray = new JsonArray();
        if (ct.isPrimitive()) {
            // int
            if (ct == Integer.TYPE) {
                var arr = (int[]) array;
                for (var i : arr) jsonArray.add(toElement(i));
            } // long
            else if (ct == Long.TYPE) {
                var arr = (long[]) array;
                for (var i : arr) jsonArray.add(toElement(i));
            } // float
            else if (ct == Float.TYPE) {
                var arr = (float[]) array;
                for (var i : arr) jsonArray.add(toElement(i));
            } // double
            else if (ct == Double.TYPE) {
                var arr = (double[]) array;
                for (var i : arr) jsonArray.add(toElement(i));
            } // short
            else if (ct == Short.TYPE) {
                var arr = (short[]) array;
                for (var i : arr) jsonArray.add(toElement(i));
            } // char
            else if (ct == Character.TYPE) {
                var arr = (char[]) array;
                for (var i : arr) jsonArray.add(toElement(i));
            } // boolean
            else if (ct == Boolean.TYPE) {
                var arr = (boolean[]) array;
                for (var i : arr) jsonArray.add(toElement(i));
            } else {
                throw new JsonException("Unsupported " + ct.getCanonicalName());
            }
        } else {
            var objs = (Object[]) array;
            for (var obj : objs) {
                jsonArray.add(toElement(obj));
            }
        }
        return jsonArray;
    }

    private JsonElement fromIterable(Iterable<?> itr) {
        var ja = new JsonArray();
        for (var it : itr) {
            ja.add(toElement(it));
        }
        return ja;
    }

    private JsonElement fromMap(Map<?, ?> map) {
        var jo = new JsonObject();
        map.forEach((key, value) -> jo.put(toKey(key), toElement(value)));
        return jo;
    }
}
