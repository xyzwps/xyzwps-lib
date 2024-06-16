package com.xyzwps.lib.json.element;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class CompactStringifierTests {

    private static void testCases(Function<JsonElement, String> stringify) {
        assertEquals("1", stringify.apply(new JsonInteger(1)));
        assertEquals("3.14", stringify.apply(new JsonDecimal(new BigDecimal("3.14"))));
        assertEquals("true", stringify.apply(JsonBoolean.TRUE));
        assertEquals("false", stringify.apply(JsonBoolean.FALSE));
        assertEquals("null", stringify.apply(JsonNull.INSTANCE));
        assertEquals("\"abc\"", stringify.apply(new JsonString("abc")));

        assertEquals("{}", stringify.apply(new JsonObject()));
        var jo = new JsonObject();
        jo.put("a", new JsonInteger(1));
        jo.put("b", new JsonInteger(2));
        assertEquals("{\"a\":1,\"b\":2}", stringify.apply(jo));

        assertEquals("[]", stringify.apply(new JsonArray()));
        var ja = new JsonArray();
        ja.add(new JsonInteger(1));
        ja.add(new JsonInteger(2));
        assertEquals("[1,2]", stringify.apply(ja));
    }

    @Test
    void testToJsonStringVisitor() {
        testCases(e -> e.visit(CompactStringifierVisitor.INSTANCE));
    }

    @Test
    void testStackToJsonString() {
        testCases(CompactStringifierStacker::stringify);
    }

}
