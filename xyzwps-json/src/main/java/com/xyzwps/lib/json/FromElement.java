package com.xyzwps.lib.json;

import com.xyzwps.lib.beans.BeanUtils;
import com.xyzwps.lib.bedrock.lang.DefaultValues;
import com.xyzwps.lib.json.element.*;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.xyzwps.lib.json.FromElementConverters.*;
import static com.xyzwps.lib.json.FromKeyConverters.*;

public final class FromElement {

    private final ConcurrentHashMap<Class<?>, FromElementConverter<?, ?>> fromElementTable;

    private final ConcurrentHashMap<Class<?>, FromKeyConverter<?>> fromKeyTable;

    private FromElement() {
        this.fromElementTable = new ConcurrentHashMap<>();
        this.fromKeyTable = new ConcurrentHashMap<>();
    }

    public static FromElement createDefault() {
        var f = new FromElement();

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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> T fromElement(JsonElement element, Type type) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(type);

        if (element instanceof JsonNull) {
            return null;
        }

        //noinspection SuspiciousMethodCalls
        FromElementConverter converter = fromElementTable.get(type);
        if (converter != null) {
            return (T) converter.convert(element);
        }

        if (type.equals(Object.class)) {
            return (T) element.toJavaObject();
        }

        switch (element) {
            case JsonObject jo -> {
                if (type instanceof Class<?> c) {
                    return (T) jsonObjectToBean(jo, c);
                }
                if (type instanceof ParameterizedType pt) {
                    var rawType = pt.getRawType();
                    if (rawType instanceof Class<?> c) {
                        if (c.isAssignableFrom(Map.class)) {
                            return (T) jsonObjectToMap(jo, pt, rawType);
                        } else {
                            return (T) jsonObjectToGenericBean(jo, pt, rawType, c);
                        }
                    }

                }
                throw new IllegalStateException("TODO: 暂支持泛型类");
            }
            case JsonArray ja -> {
                return switch (type) {
                    case ParameterizedType pt -> (T) jsonArrayToList(ja, pt);
                    case GenericArrayType gat -> (T) jsonArrayToGenericArray(ja, gat);
                    case Class c -> {
                        if (c.isArray()) {
                            yield (T) jsonArrayToArray(ja, c);
                        }
                        throw new JsonException("Cannot parse json array to " + c.getCanonicalName());
                    }
                    default -> throw new JsonException("Cannot parse json array to " + type);
                };
            }
            case JsonString js -> {
                if (type instanceof Class<?> c) {
                    if (c.isEnum()) {
                        return (T) Enum.valueOf((Class<? extends Enum>) c, js.value());
                    }
                }
                throw new JsonException("Cannot parse json string to " + type);
            }
            default -> {
                throw new JsonException(String.format("Cannot convert to %s(%s) from %s",
                        type.getTypeName(), type.getClass().getCanonicalName(), element.getClass().getSimpleName()));
            }
        }
    }

    private Object jsonObjectToMap(JsonObject jo, ParameterizedType pt, Type rawType) {
        var typeArguments = pt.getActualTypeArguments();
        var keyType = typeArguments[0];
        var valueType = typeArguments[1];

        var toKeyConverter = fromKeyTable.get(keyType);
        var map = rawType.equals(TreeMap.class) ? new TreeMap<>() : new HashMap<>();
        jo.forEach((key, value) -> map.put(toKeyConverter.convert(key), fromElement(value, valueType)));
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
                var propValue = propElement == null ? DefaultValues.get(prop.type()) : fromElement(propElement, actualType);
                parsedProps.put(propName, propValue);
            } else {
                var propName = prop.name();
                var propElement = jo.get(propName);
                var propValue = propElement == null ? DefaultValues.get(prop.type()) : fromElement(propElement, prop.type());
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
                    : fromElement(propElement, prop.type());
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
            ja.forEach((arrayItem, i) -> Array.set(array, i, fromElement(arrayItem, elementType)));
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
                    if (fromElement(arrayItem, elementType) instanceof Short s) {
                        sa[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == int.class) {
                var ia = (int[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (fromElement(arrayItem, elementType) instanceof Integer s) {
                        ia[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == long.class) {
                var la = (long[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (fromElement(arrayItem, elementType) instanceof Long s) {
                        la[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == float.class) {
                var fa = (float[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (fromElement(arrayItem, elementType) instanceof Float s) {
                        fa[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == double.class) {
                var da = (double[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (fromElement(arrayItem, elementType) instanceof Double s) {
                        da[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == boolean.class) {
                var ba = (boolean[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (fromElement(arrayItem, elementType) instanceof Boolean s) {
                        ba[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == char.class) {
                var ca = (char[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (fromElement(arrayItem, elementType) instanceof Character s) {
                        ca[i] = s;
                    } else {
                        throw new JsonException();
                    }
                });
            } else if (elementType == byte.class) {
                var ba = (byte[]) array;
                ja.forEach((arrayItem, i) -> {
                    if (fromElement(arrayItem, elementType) instanceof Byte s) {
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
            ja.forEach((arrayItem, i) -> oa[i] = fromElement(arrayItem, elementType));
        }
        return array;
    }

    private List<?> fromJsonArray(JsonArray ja, Type elementType, List<?> list) {
        ja.forEach((arrayItem) -> list.add(fromElement(arrayItem, elementType)));
        return list;
    }
}
