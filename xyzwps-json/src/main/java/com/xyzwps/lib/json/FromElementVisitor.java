package com.xyzwps.lib.json;

import com.xyzwps.lib.beans.BeanUtils;
import com.xyzwps.lib.bedrock.lang.DefaultValues;
import com.xyzwps.lib.json.element.*;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static com.xyzwps.lib.json.FromElementConverters.*;
import static com.xyzwps.lib.json.FromKeyConverters.KEY_TO_STRING;

public final class FromElementVisitor implements JsonElementVisitor2<Object, Type>, FromElement {

    private final ConcurrentHashMap<Type, FromElementConverter<?, ?>> fromElementTable;

    private final ConcurrentHashMap<Type, FromKeyConverter<?>> fromKeyTable;

    private FromElementVisitor() {
        this.fromElementTable = new ConcurrentHashMap<>();
        this.fromKeyTable = new ConcurrentHashMap<>();
    }

    public static FromElementVisitor createDefault() {
        var f = new FromElementVisitor();

        f.addFromElementConverter(Short.class, TO_SHORT);
        f.addFromElementConverter(short.class, TO_SHORT);
        f.addFromElementConverter(Integer.class, TO_INT);
        f.addFromElementConverter(int.class, TO_INT);
        f.addFromElementConverter(Long.class, TO_LONG);
        f.addFromElementConverter(long.class, TO_LONG);
        f.addFromElementConverter(BigInteger.class, TO_BIGINT);
        f.addFromElementConverter(Float.class, TO_FLOAT);
        f.addFromElementConverter(float.class, TO_FLOAT);
        f.addFromElementConverter(Double.class, TO_DOUBLE);
        f.addFromElementConverter(double.class, TO_DOUBLE);
        f.addFromElementConverter(BigDecimal.class, TO_BIGDECIMAL);
        f.addFromElementConverter(Boolean.class, TO_BOOLEAN);
        f.addFromElementConverter(boolean.class, TO_BOOLEAN);
        f.addFromElementConverter(Character.class, TO_CHAR);
        f.addFromElementConverter(char.class, TO_CHAR);
        f.addFromElementConverter(String.class, TO_STRING);
        f.addFromElementConverter(Map.class, TO_MAP.apply(f));
        f.addFromElementConverter(List.class, TO_LIST.apply(f));

        f.addFromKeyConverter(String.class, KEY_TO_STRING);

        return f;
    }

    public void addFromElementConverter(Class<?> type, FromElementConverter<?, ?> converter) {
        this.fromElementTable.put(Objects.requireNonNull(type), Objects.requireNonNull(converter));
    }

    public void addFromKeyConverter(Class<?> type, FromKeyConverter<?> converter) {
        this.fromKeyTable.put(Objects.requireNonNull(type), Objects.requireNonNull(converter));
    }

    private Object jsonObjectToMap(JsonObject jo, ParameterizedType pt, Type rawType) {
        var typeArguments = pt.getActualTypeArguments();
        var keyType = typeArguments[0];
        var valueType = typeArguments[1];

        var toKeyConverter = fromKeyTable.get(keyType);
        var map = rawType.equals(TreeMap.class) ? new TreeMap<>() : new HashMap<>();
        jo.forEach((key, value) -> map.put(toKeyConverter.convert(key), value.visit(valueType, this)));
        return map;
    }

    private Object jsonObjectToGenericBean(JsonObject jo, ParameterizedType pt, Type rawType, Class<?> rawTypeClass) {
        var beanInfo = BeanUtils.getBeanInfoFromClass(rawTypeClass);
        var parsedProps = new HashMap<String, Object>();
        beanInfo.getBeanProperties().forEach(prop -> {
            var propType = prop.type();
            if (propType instanceof TypeVariable<?> tv) {
                var actualType = getActualType(pt, (Class<?>) rawType, tv);
                var propName = prop.name();
                var propElement = jo.get(propName);
                var propValue = propElement == null ? DefaultValues.get(prop.type()) : propElement.visit(actualType, this);
                parsedProps.put(propName, propValue);
            } else {
                var propName = prop.name();
                var propElement = jo.get(propName);
                var propValue = propElement == null ? DefaultValues.get(prop.type()) : propElement.visit(prop.type(), this);
                parsedProps.put(propName, propValue);
            }
        });
        return beanInfo.create(parsedProps);
    }

    /**
     * @param pt      where to get type variable values
     * @param rawType where to get generic variables
     * @param tv      generic variable
     * @return actual type corresponding to the <code>tv</code> from <code>pt</code>
     */
    private static Type getActualType(ParameterizedType pt, Class<?> rawType, TypeVariable<?> tv) {
        var typeParams = rawType.getTypeParameters();
        int index = -1;
        for (int i = 0; i < typeParams.length; i++) {
            var it = typeParams[i];
            if (it.equals(tv)) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            throw new IllegalStateException();
        }
        return pt.getActualTypeArguments()[index];
    }

    private Object jsonObjectToBean(JsonObject jo, Class<?> c) {
        var beanInfo = BeanUtils.getBeanInfoFromClass(c);
        var parsedProps = new HashMap<String, Object>();
        beanInfo.getBeanProperties().forEach(prop -> {
            var propName = prop.name();
            var propElement = jo.get(propName);
            var propValue = propElement == null
                    ? DefaultValues.get(prop.type())
                    : propElement.visit(prop.type(), this);
            parsedProps.put(propName, propValue);
        });
        return beanInfo.create(parsedProps);
    }

    private Object jsonArrayToList(JsonArray ja, ParameterizedType pt) {
        var rawType = pt.getRawType();
        var elementType = pt.getActualTypeArguments()[0];
        if (rawType.equals(ArrayList.class) || rawType.equals(List.class)) {
            return fromJsonArray(ja, elementType, new ArrayList<>(ja.length()));
        }
        if (rawType.equals(LinkedList.class)) {
            return fromJsonArray(ja, elementType, new LinkedList<>());
        }
        throw new JsonException("Cannot parse json array to " + pt);
    }

    private Object jsonArrayToGenericArray(JsonArray ja, GenericArrayType gat) {
        var elementType = gat.getGenericComponentType();
        if (elementType instanceof ParameterizedType pt) {
            var array = Array.newInstance((Class<?>) pt.getRawType(), ja.length());
            ja.forEach((arrayItem, i) -> Array.set(array, i, arrayItem.visit(elementType, this)));
            return array;
        } else {
            throw new JsonException("Cannot parse json array to " + gat);
        }
    }

    private Object jsonArrayToArray(JsonArray ja, Class<?> c) {
        var elementType = c.getComponentType();
        var array = Array.newInstance(elementType, ja.length());
        if (elementType.isPrimitive()) {
            if (elementType == short.class) {
                var sa = (short[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (arrayItem.visit(elementType, this) instanceof Short s) {
                        sa[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == int.class) {
                var ia = (int[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (arrayItem.visit(elementType, this) instanceof Integer s) {
                        ia[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == long.class) {
                var la = (long[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (arrayItem.visit(elementType, this) instanceof Long s) {
                        la[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == float.class) {
                var fa = (float[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (arrayItem.visit(elementType, this) instanceof Float s) {
                        fa[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == double.class) {
                var da = (double[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (arrayItem.visit(elementType, this) instanceof Double s) {
                        da[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == boolean.class) {
                var ba = (boolean[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (arrayItem.visit(elementType, this) instanceof Boolean s) {
                        ba[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == char.class) {
                var ca = (char[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (arrayItem.visit(elementType, this) instanceof Character s) {
                        ca[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == byte.class) {
                var ba = (byte[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (arrayItem.visit(elementType, this) instanceof Byte s) {
                        ba[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else {
                throw new JsonException("Cannot parse json array to primitive array of " + elementType.getName());
            }
        } else {
            var oa = (Object[]) array;
            ja.forEach((arrayItem, i) -> oa[i] = arrayItem.visit(elementType, this));
        }
        return array;
    }

    private List<Object> fromJsonArray(JsonArray ja, Type elementType, List<Object> list) {
        ja.forEach((arrayItem) -> list.add(arrayItem.visit(elementType, this)));
        return list;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object commonMap(Type type, JsonElement element, Supplier<Object> elseSupplier) {
        FromElementConverter converter = fromElementTable.get(type);
        if (converter != null) {
            return converter.convert(element);
        }

        if (type.equals(Object.class)) {
            return element.toJavaObject();
        }

        return elseSupplier.get();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object visit(Type type, JsonArray ja) {
        return commonMap(type, ja, () -> switch (type) {
            case ParameterizedType pt -> jsonArrayToList(ja, pt);
            case GenericArrayType gat -> jsonArrayToGenericArray(ja, gat);
            case Class c -> {
                if (c.isArray()) {
                    yield jsonArrayToArray(ja, c);
                }
                throw new JsonException("Cannot parse json array to " + c.getCanonicalName());
            }
            default -> throw new JsonException("Cannot parse json array to " + type);
        });
    }

    @Override
    public Object visit(Type type, JsonBoolean jb) {
        return commonMap(type, jb, () -> {
            throw new JsonException(String.format("Cannot convert to %s(%s) from JsonBoolean",
                    type.getTypeName(), type.getClass().getCanonicalName()));
        });
    }

    @Override
    public Object visit(Type type, JsonDecimal jd) {
        return commonMap(type, jd, () -> {
            throw new JsonException(String.format("Cannot convert to %s(%s) from JsonDecimal",
                    type.getTypeName(), type.getClass().getCanonicalName()));
        });
    }

    @Override
    public Object visit(Type type, JsonInteger ji) {
        return commonMap(type, ji, () -> {
            throw new JsonException(String.format("Cannot convert to %s(%s) from JsonInteger",
                    type.getTypeName(), type.getClass().getCanonicalName()));
        });
    }

    @Override
    public Object visit(Type type, JsonNull jn) {
        return commonMap(type, jn, () -> null);
    }

    @Override
    public Object visit(Type type, JsonObject jo) {
        return commonMap(type, jo, () -> {
            if (type instanceof Class<?> c) {
                return jsonObjectToBean(jo, c);
            }
            if (type instanceof ParameterizedType pt) {
                var rawType = pt.getRawType();
                if (rawType instanceof Class<?> c) {
                    if (c.isAssignableFrom(Map.class)) {
                        return jsonObjectToMap(jo, pt, rawType);
                    } else {
                        return jsonObjectToGenericBean(jo, pt, rawType, c);
                    }
                }

            }
            throw new IllegalStateException("TODO: 暂支持泛型类");
        });
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object visit(Type type, JsonString js) {
        return commonMap(type, js, () -> {
            if (type instanceof Class<?> c) {
                if (c.isEnum()) {
                    return Enum.valueOf((Class<? extends Enum>) c, js.value());
                }
            }
            throw new JsonException("Cannot parse JsonString to " + type);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromElement(JsonElement element, Type type) {
        return (T) element.visit(type, this);
    }
}
