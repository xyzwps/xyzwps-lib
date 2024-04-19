package com.xyzwps.lib.json;

import com.xyzwps.lib.beans.BeanUtils;
import com.xyzwps.lib.bedrock.lang.DefaultValues;
import com.xyzwps.lib.bedrock.lang.Types;
import com.xyzwps.lib.json.element.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class FromElement {

    private final ConcurrentHashMap<Class<?>, FromElementConverter<?, ?>> fromElementTable;

    private FromElement() {
        this.fromElementTable = new ConcurrentHashMap<>();
    }

    public static FromElement createDefault() {
        var f = new FromElement();

        f.addFromElementConverter(Short.class, (e) -> switch (e) {
            case JsonInteger i -> i.value().shortValue();
            case JsonDecimal d -> d.value().shortValue();
            default -> throw new JsonException("Cannot convert to Short from " + e.getClass().getSimpleName());
        });
        f.addFromElementConverter(Integer.class, (e) -> switch (e) {
            case JsonInteger i -> i.value().intValue();
            case JsonDecimal d -> d.value().intValue();
            default -> throw new JsonException("Cannot convert to Integer from " + e.getClass().getSimpleName());
        });
        f.addFromElementConverter(Long.class, (e) -> switch (e) {
            case JsonInteger i -> i.value().longValue();
            case JsonDecimal d -> d.value().longValue();
            default -> throw new JsonException("Cannot convert to Long from " + e.getClass().getSimpleName());
        });
        f.addFromElementConverter(BigInteger.class, (e) -> switch (e) {
            case JsonInteger i -> i.value();
            case JsonDecimal d -> d.value().toBigInteger();
            default -> throw new JsonException("Cannot convert to BigInteger from " + e.getClass().getSimpleName());
        });
        f.addFromElementConverter(Float.class, (e) -> switch (e) {
            case JsonInteger i -> i.value().floatValue();
            case JsonDecimal d -> d.value().floatValue();
            default -> throw new JsonException("Cannot convert to Float from " + e.getClass().getSimpleName());
        });
        f.addFromElementConverter(Double.class, (e) -> switch (e) {
            case JsonInteger i -> i.value().doubleValue();
            case JsonDecimal d -> d.value().doubleValue();
            default -> throw new JsonException("Cannot convert to Double from " + e.getClass().getSimpleName());
        });
        f.addFromElementConverter(BigDecimal.class, (e) -> switch (e) {
            case JsonInteger i -> new BigDecimal(i.value());
            case JsonDecimal d -> d.value();
            default -> throw new JsonException("Cannot convert to BigDecimal from " + e.getClass().getSimpleName());
        });
        f.addFromElementConverter(Boolean.class, (e) -> switch (e) {
            case JsonInteger i -> !BigInteger.ZERO.equals(i.value());
            case JsonBoolean b -> b.value;
            default -> throw new JsonException("Cannot convert to Boolean from " + e.getClass().getSimpleName());
        });
        f.addFromElementConverter(String.class, (e) -> switch (e) {
            case JsonBoolean it -> it.value ? "true" : "false";
            case JsonDecimal it -> it.value().toString();
            case JsonInteger it -> it.value().toString();
            case JsonString it -> it.value();
            default -> throw new JsonException("Cannot convert to String from " + e.getClass().getSimpleName());
        });
        f.addFromElementConverter(Map.class, (e) -> switch (e) {
            case JsonObject o -> {
                var map = new HashMap<String, Object>();
                o.forEach((key, element) -> map.put(key, switch (element) {
                    case JsonNull it -> f.fromElement(it, Object.class);
                    case JsonArray it -> f.fromElement(it, List.class);
                    case JsonBoolean it -> f.fromElement(it, Boolean.class);
                    case JsonDecimal it -> f.fromElement(it, BigDecimal.class);
                    case JsonInteger it -> f.fromElement(it, BigInteger.class);
                    case JsonObject it -> f.fromElement(it, Map.class);
                    case JsonString it -> f.fromElement(it, String.class);
                }));
                yield map;
            }
            default -> throw new JsonException("Cannot convert to Map from " + e.getClass().getSimpleName());
        });
        f.addFromElementConverter(List.class, (e) -> switch (e) {
            case JsonArray a -> {
                var list = new ArrayList<>();
                a.forEach((element) -> list.add(switch (element) {
                    case JsonNull it -> f.fromElement(it, Object.class);
                    case JsonArray it -> f.fromElement(it, List.class);
                    case JsonBoolean it -> f.fromElement(it, Boolean.class);
                    case JsonDecimal it -> f.fromElement(it, BigDecimal.class);
                    case JsonInteger it -> f.fromElement(it, BigInteger.class);
                    case JsonObject it -> f.fromElement(it, Map.class);
                    case JsonString it -> f.fromElement(it, String.class);
                }));
                yield list;
            }
            default -> throw new JsonException("Cannot convert to List from " + e.getClass().getSimpleName());
        });

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

        if (element instanceof JsonNull jn) {
            return null;
        }

        FromElementConverter converter = fromElementTable.get(type);
        if (converter != null) {
            return (T) converter.convert(element);
        }

        if (element instanceof JsonObject jo) {
            if (type instanceof Class<?> c) {
                var beanInfo = BeanUtils.getBeanInfoFromClass(c);
                var parsedProps = new HashMap<String, Object>();
                beanInfo.getBeanProperties().forEach(prop -> {
                    var propName = prop.name();
                    var propElement = jo.get(propName);
                    var propValue = propElement == null ? DefaultValues.get(prop.type()) : fromElement(propElement, prop.type());
                    parsedProps.put(propName, propValue);
                });
                return (T) beanInfo.create(parsedProps);
            } else {
                throw new IllegalStateException("TODO: 暂支持泛型类");
            }
        }
        throw new JsonException("Cannot convert to " + type.getTypeName() + " from " + element.getClass().getSimpleName());
    }


    // TODO: 处理泛型类，如 List<Integer> 之类的
}
