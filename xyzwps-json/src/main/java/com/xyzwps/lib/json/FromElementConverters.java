package com.xyzwps.lib.json;

import com.xyzwps.lib.json.element.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class FromElementConverters {

    public static final FromElementConverter<JsonElement, Short> TO_SHORT = (e) -> switch (e) {
        case JsonInteger i -> i.value().shortValue();
        case JsonDecimal d -> d.value().shortValue();
        default -> throw new JsonException("Cannot convert to Short from " + e.getClass().getSimpleName());
    };

    public static final FromElementConverter<JsonElement, Integer> TO_INT = (e) -> switch (e) {
        case JsonInteger i -> i.value().intValue();
        case JsonDecimal d -> d.value().intValue();
        default -> throw new JsonException("Cannot convert to Integer from " + e.getClass().getSimpleName());
    };

    public static final FromElementConverter<JsonElement, Long> TO_LONG = (e) -> switch (e) {
        case JsonInteger i -> i.value().longValue();
        case JsonDecimal d -> d.value().longValue();
        default -> throw new JsonException("Cannot convert to Long from " + e.getClass().getSimpleName());
    };

    public static final FromElementConverter<JsonElement, BigInteger> TO_BIGINT = (e) -> switch (e) {
        case JsonInteger i -> i.value();
        case JsonDecimal d -> d.value().toBigInteger();
        default -> throw new JsonException("Cannot convert to BigInteger from " + e.getClass().getSimpleName());
    };

    public static final FromElementConverter<JsonElement, Float> TO_FLOAT = (e) -> switch (e) {
        case JsonInteger i -> i.value().floatValue();
        case JsonDecimal d -> d.value().floatValue();
        default -> throw new JsonException("Cannot convert to Float from " + e.getClass().getSimpleName());
    };

    public static final FromElementConverter<JsonElement, Double> TO_DOUBLE = (e) -> switch (e) {
        case JsonInteger i -> i.value().doubleValue();
        case JsonDecimal d -> d.value().doubleValue();
        default -> throw new JsonException("Cannot convert to Double from " + e.getClass().getSimpleName());
    };

    public static final FromElementConverter<JsonElement, BigDecimal> TO_BIGDECIMAL = (e) -> switch (e) {
        case JsonInteger i -> new BigDecimal(i.value());
        case JsonDecimal d -> d.value();
        default -> throw new JsonException("Cannot convert to BigDecimal from " + e.getClass().getSimpleName());
    };

    public static final FromElementConverter<JsonElement, Boolean> TO_BOOLEAN = (e) -> switch (e) {
        case JsonInteger i -> !BigInteger.ZERO.equals(i.value());
        case JsonBoolean b -> b.value;
        default -> throw new JsonException("Cannot convert to Boolean from " + e.getClass().getSimpleName());
    };

    public static final FromElementConverter<JsonElement, Character> TO_CHAR = (e) -> switch (e) {
        case JsonString s -> {
            if (s.value().length() != 1) {
                throw new JsonException("Cannot convert to Character from String with length != 1");
            }
            yield s.value().charAt(0);
        }
        default -> throw new JsonException("Cannot convert to Character from " + e.getClass().getSimpleName());
    };

    public static final FromElementConverter<JsonElement, String> TO_STRING = (e) -> switch (e) {
        case JsonBoolean it -> it.value ? "true" : "false";
        case JsonDecimal it -> it.value().toString();
        case JsonInteger it -> it.value().toString();
        case JsonString it -> it.value();
        default -> throw new JsonException("Cannot convert to String from " + e.getClass().getSimpleName());
    };

    public static final Function<FromElement, FromElementConverter<JsonElement, Map>> TO_MAP = (f) -> (e) -> switch (e) {
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
    };

    public static final Function<FromElement, FromElementConverter<JsonElement, List>> TO_LIST = (f) -> (e) -> switch (e) {
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
    };

    private FromElementConverters() throws IllegalAccessException {
        throw new IllegalAccessException("??");
    }
}
