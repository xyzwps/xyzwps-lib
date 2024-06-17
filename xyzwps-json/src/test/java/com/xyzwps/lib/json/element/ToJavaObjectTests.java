package com.xyzwps.lib.json.element;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.bedrock.lang.Equals.*;

class ToJavaObjectTests {

    @Test
    void testToJavaObjectVisitor() {
        testCases(e -> e.visit(ToJavaObjectVisitor.INSTANCE));
    }

    @Test
    void testToJavaObjectStacker() {
        testCases(ToJavaObjectStacker::toJavaObject);
    }

    static void testCases(Function<JsonElement, Object> toJavaObject) {
        assertEquals(BigInteger.ONE, toJavaObject.apply(new JsonInteger(1)));
        assertEquals(new BigDecimal("3.14"), toJavaObject.apply(new JsonDecimal(new BigDecimal("3.14"))));
        assertEquals(true, toJavaObject.apply(JsonBoolean.TRUE));
        assertEquals(false, toJavaObject.apply(JsonBoolean.FALSE));
        assertNull(toJavaObject.apply(JsonNull.INSTANCE));
        assertEquals("abc", toJavaObject.apply(new JsonString("abc")));

        assertTrue(mapEntryEquals(new HashMap<>(), (Map<?, ?>) toJavaObject.apply(new JsonObject())));

        var jo = new JsonObject();
        jo.put("a", new JsonInteger(1));
        jo.put("b", new JsonInteger(2));
        var map = new HashMap<>();
        map.put("a", BigInteger.valueOf(1));
        map.put("b", BigInteger.valueOf(2));
        assertTrue(mapEntryEquals(map, (Map<Object, Object>) toJavaObject.apply(jo)));

        assertIterableEquals(List.of(), (List<?>) toJavaObject.apply(new JsonArray()));

        var ja = new JsonArray();
        ja.add(new JsonInteger(1));
        ja.add(new JsonInteger(2));
        var list = new ArrayList<>();
        list.add(BigInteger.ONE);
        list.add(BigInteger.valueOf(2));
        assertIterableEquals(list, (List<?>) toJavaObject.apply(ja));
    }


}
