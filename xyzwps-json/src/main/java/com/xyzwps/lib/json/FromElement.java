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

    // TODO: 支持日期时间

    public void addFromElementConverter(Class<?> type, FromElementConverter<?, ?> converter) {
        this.fromElementTable.put(Objects.requireNonNull(type), Objects.requireNonNull(converter));
    }

    public void addFromKeyConverter(Class<?> type, FromKeyConverter<?> converter) {
        this.fromKeyTable.put(Objects.requireNonNull(type), Objects.requireNonNull(converter));
    }

    public <T> T fromElement(JsonElement element, Type type) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(type);

        if (element instanceof JsonNull) {
            return null;
        }

        //noinspection SuspiciousMethodCalls, rawtypes
        FromElementConverter converter = fromElementTable.get(type);
        if (converter != null) {
            //noinspection unchecked
            return (T) converter.convert(element);
        }

        if (type.equals(Object.class)) {
            return (T) element.toJavaObject();
        }

        switch (element) {
            case JsonObject jo -> {
                if (type instanceof Class<?> c) {
                    var beanInfo = BeanUtils.getBeanInfoFromClass(c);
                    var parsedProps = new HashMap<String, Object>();
                    beanInfo.getBeanProperties().forEach(prop -> {
                        var propName = prop.name();
                        var propElement = jo.get(propName);
                        var propValue = propElement == null ? DefaultValues.get(prop.type()) : fromElement(propElement, prop.type());
                        parsedProps.put(propName, propValue);
                    });
                    //noinspection unchecked
                    return (T) beanInfo.create(parsedProps);
                } else if (type instanceof ParameterizedType pt) {
                    var rawType = pt.getRawType();
                    if (rawType instanceof Class<?> c) {
                        if (c.isAssignableFrom(Map.class)) {

                            // TODO: 更多种类的 map
                            var typeArguments = pt.getActualTypeArguments();
                            var keyType = typeArguments[0];
                            var valueType = typeArguments[1];

                            var toKeyConverter = fromKeyTable.get(keyType);
                            var map = new HashMap();

                            jo.forEach((key, value) -> map.put(toKeyConverter.convert(key), fromElement(value, valueType)));
                            //noinspection unchecked
                            return (T) map;
                        } else {
                            var beanInfo = BeanUtils.getBeanInfoFromClass(c);
                            var parsedProps = new HashMap<String, Object>();
                            beanInfo.getBeanProperties().forEach(prop -> {
                                var propType = prop.type();
                                if (propType instanceof TypeVariable<?> tv) {
                                    // region get actual type
                                    var typeParams = ((Class<?>) rawType).getTypeParameters();
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
                                    var actualType = pt.getActualTypeArguments()[index];
                                    // endregion
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
                            //noinspection unchecked
                            return (T) beanInfo.create(parsedProps);
                        }
                    }
                    throw new IllegalStateException("TODO: 暂支持泛型类");
                } else {
                    throw new IllegalStateException("TODO: 暂支持泛型类");
                }
            }
            case JsonArray ja -> {
                switch (type) {
                    case ParameterizedType pt -> {
                        var rawType = pt.getRawType();
                        //noinspection IfCanBeSwitch
                        if (rawType.equals(ArrayList.class)) {
                            // noinspection unchecked
                            return (T) fromJsonArray(ja, pt.getActualTypeArguments()[0], new ArrayList<>(ja.length()));
                        }
                        if (rawType.equals(LinkedList.class)) {
                            // noinspection unchecked
                            return (T) fromJsonArray(ja, pt.getActualTypeArguments()[0], new LinkedList<>());
                        }
                        if (rawType.equals(List.class)) {
                            // noinspection unchecked
                            return (T) fromJsonArray(ja, pt.getActualTypeArguments()[0], new ArrayList<>(ja.length()));
                        }
                        throw new JsonException(String.format("Cannot convert to %s from %s",
                                type.getTypeName(), element.getClass().getSimpleName()));
                    }
                    //noinspection rawtypes
                    case Class c -> {
                        if (c.isArray()) {
                            var elementType = c.getComponentType();
                            var array = Array.newInstance(elementType, ja.length());
                            if (elementType.isPrimitive()) {
                                if (elementType == short.class) {
                                    var sa = (short[]) array;
                                    // noinspection all
                                    ja.forEach((arrayItem, i) -> sa[i] = fromElement(arrayItem, elementType));
                                } else if (elementType == int.class) {
                                    var ia = (int[]) array;
                                    // noinspection all
                                    ja.forEach((arrayItem, i) -> ia[i] = fromElement(arrayItem, elementType));
                                } else if (elementType == long.class) {
                                    var la = (long[]) array;
                                    // noinspection all
                                    ja.forEach((arrayItem, i) -> la[i] = fromElement(arrayItem, elementType));
                                } else if (elementType == float.class) {
                                    var fa = (float[]) array;
                                    // noinspection all
                                    ja.forEach((arrayItem, i) -> fa[i] = fromElement(arrayItem, elementType));
                                } else if (elementType == double.class) {
                                    var da = (double[]) array;
                                    // noinspection all
                                    ja.forEach((arrayItem, i) -> da[i] = fromElement(arrayItem, elementType));
                                } else if (elementType == boolean.class) {
                                    var ba = (boolean[]) array;
                                    // noinspection all
                                    ja.forEach((arrayItem, i) -> ba[i] = fromElement(arrayItem, elementType));
                                } else if (elementType == char.class) {
                                    var ca = (char[]) array;
                                    // noinspection all
                                    ja.forEach((arrayItem, i) -> ca[i] = fromElement(arrayItem, elementType));
                                } else if (elementType == byte.class) {
                                    var ba = (byte[]) array;
                                    // noinspection all
                                    ja.forEach((arrayItem, i) -> ba[i] = fromElement(arrayItem, elementType));
                                } else {
                                    throw new JsonException(String.format("Cannot convert to %s from %s",
                                            type.getTypeName(), element.getClass().getSimpleName()));
                                }
                            } else {
                                var oa = (Object[]) array;
                                ja.forEach((arrayItem, i) -> oa[i] = fromElement(arrayItem, elementType));
                            }
                            // noinspection unchecked
                            return (T) array;
                        } else {
                            throw new JsonException(String.format("Cannot convert to %s from %s",
                                    type.getTypeName(), element.getClass().getSimpleName()));
                        }
                    }
                    case GenericArrayType gat -> {
                        var elementType = gat.getGenericComponentType();
                        if (elementType instanceof ParameterizedType pt) {
                            var array = Array.newInstance((Class<?>) pt.getRawType(), ja.length());
                            ja.forEach((arrayItem, i) -> Array.set(array, i, fromElement(arrayItem, elementType)));
                            // noinspection unchecked
                            return (T) array;
                        } else {
                            throw new JsonException(String.format("Cannot convert to %s from %s",
                                    type.getTypeName(), element.getClass().getSimpleName()));
                        }
                    }
                    default -> {
                        throw new RuntimeException();
                    }
                }
            }
            case JsonString js -> {
                if (type instanceof Class<?> c) {
                    if (c.isEnum()) {
                        // noinspection unchecked, rawtypes
                        return (T) Enum.valueOf((Class<? extends Enum>) c, js.value());
                    }
                }
                throw new JsonException(String.format("Cannot convert to %s(%s) from %s",
                        type.getTypeName(), type.getClass().getCanonicalName(), element.getClass().getSimpleName()));
            }
            default -> {
                throw new JsonException(String.format("Cannot convert to %s(%s) from %s",
                        type.getTypeName(), type.getClass().getCanonicalName(), element.getClass().getSimpleName()));
            }
        }
    }

    private List<?> fromJsonArray(JsonArray ja, Type elementType, List<?> list) {
        ja.forEach((arrayItem) -> list.add(fromElement(arrayItem, elementType)));
        return list;
    }
}
