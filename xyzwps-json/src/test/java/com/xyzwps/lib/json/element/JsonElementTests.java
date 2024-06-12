package com.xyzwps.lib.json.element;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class JsonElementTests {

    @Test
    void testToPrettyString() {
        var ji = new JsonInteger(12);
        assertEquals("12", ji.toPrettyString());

        var jb = JsonBoolean.TRUE;
        assertEquals("true", jb.toPrettyString());

        var jd = new JsonDecimal(new BigDecimal("12.34"));
        assertEquals("12.34", jd.toPrettyString());

        var jn = JsonNull.INSTANCE;
        assertEquals("null", jn.toPrettyString());

        var js = new JsonString("a\nbc");
        assertEquals("\"a\\nbc\"", js.toPrettyString());

        var ja = new JsonArray();
        assertEquals("[]", ja.toPrettyString());

        ja.add(ji);
        ja.add(jb);
        assertEquals("""
                    [
                        12,
                        true
                    ]""", ja.toPrettyString());

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
                    ]""", ja.toPrettyString());

        var jo = new JsonObject();
        assertEquals("{}", jo.toPrettyString());

        jo.put("ji", ji);
        jo.put("jb", jb);
        assertEquals("""
                    {
                        "jb": true,
                        "ji": 12
                    }""", jo.toPrettyString());

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
                    }""", jo.toPrettyString());

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
                    ]""", ja.toPrettyString());

    }
}
