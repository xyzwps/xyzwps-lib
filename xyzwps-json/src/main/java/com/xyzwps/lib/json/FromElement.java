package com.xyzwps.lib.json;

import com.xyzwps.lib.beans.BeanUtils;
import com.xyzwps.lib.bedrock.lang.DefaultValues;
import com.xyzwps.lib.json.element.*;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.xyzwps.lib.json.FromElementConverters.*;

public final class FromElement {

    private final ConcurrentHashMap<Class<?>, FromElementConverter<?, ?>> fromElementTable;

    private FromElement() {
        this.fromElementTable = new ConcurrentHashMap<>();
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

        return f;
    }

    // TODO: 支持日期时间
    // TODO: 测试

    public void addFromElementConverter(Class<?> type, FromElementConverter<?, ?> converter) {
        this.fromElementTable.put(Objects.requireNonNull(type), Objects.requireNonNull(converter));
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

//                        // TODO: handle multi-dim array
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


    // TODO: 处理泛型类，如 List<Integer> 之类的
}
