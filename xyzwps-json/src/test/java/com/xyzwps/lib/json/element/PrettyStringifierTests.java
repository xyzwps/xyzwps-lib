package com.xyzwps.lib.json.element;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrettyStringifierTests {

    @Test
    void testPrettyStringifierVisitor() {
        testCases(e -> e.visit(0, new PrettyStringifierVisitor()).toString());
    }

    @Test
    void testPrettyStringifierStacker() {
        testCases(PrettyStringifierStacker::stringify);
    }

    static void testCases(Function<JsonElement, String> stringify) {
        var ji = new JsonInteger(12);
        assertEquals("12", stringify.apply(ji));

        var jb = JsonBoolean.TRUE;
        assertEquals("true", stringify.apply(jb));

        var jd = new JsonDecimal(new BigDecimal("12.34"));
        assertEquals("12.34", stringify.apply(jd));

        var jn = JsonNull.INSTANCE;
        assertEquals("null", stringify.apply(jn));

        var js = new JsonString("a\nbc");
        assertEquals("\"a\\nbc\"", stringify.apply(js));

        var ja = new JsonArray();
        assertEquals("[]", stringify.apply(ja));

        ja.add(ji);
        ja.add(jb);
        assertEquals("""
                [
                    12,
                    true
                ]""", stringify.apply(ja));

        var ja2 = new JsonArray();
        ja2.add(js);
        ja.add(ja2);
        assertEquals("""
                [
                    12,
                    true,
                    [
                        "a\\nbc"
                    ]
                ]""", stringify.apply(ja));

        var jo = new JsonObject();
        assertEquals("{}", stringify.apply(jo));

        jo.put("ji", ji);
        jo.put("jb", jb);
        assertEquals("""
                {
                    "jb": true,
                    "ji": 12
                }""", stringify.apply(jo));

        var jo2 = new JsonObject();
        jo2.put("a\nbc", js);
        jo.put("jo2", jo2);
        assertEquals("""
                {
                    "jb": true,
                    "ji": 12,
                    "jo2": {
                        "a\\nbc": "a\\nbc"
                    }
                }""", stringify.apply(jo));

        ja.add(jo2);
        assertEquals("""
                [
                    12,
                    true,
                    [
                        "a\\nbc"
                    ],
                    {
                        "a\\nbc": "a\\nbc"
                    }
                ]""", stringify.apply(ja));
    }
}
