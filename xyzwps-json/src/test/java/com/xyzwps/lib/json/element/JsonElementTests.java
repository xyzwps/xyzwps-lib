package com.xyzwps.lib.json.element;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class JsonElementTests {

    @Test
    void testStringify() {
        var ji = new JsonInteger(12);
        assertEquals("12", ji.stringify(true));

        var jb = JsonBoolean.TRUE;
        assertEquals("true", jb.stringify(true));

        var jd = new JsonDecimal(new BigDecimal("12.34"));
        assertEquals("12.34", jd.stringify(true));

        var jn = JsonNull.INSTANCE;
        assertEquals("null", jn.stringify(true));

        var js = new JsonString("a\nbc");
        assertEquals("\"a\\nbc\"", js.stringify(true));

        var ja = new JsonArray();
        assertEquals("[]", ja.stringify(true));

        ja.add(ji);
        ja.add(jb);
        assertEquals("""
                    [
                        12,
                        true
                    ]""", ja.stringify(true));

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
                    ]""", ja.stringify(true));

        var jo = new JsonObject();
        assertEquals("{}", jo.stringify(true));

        jo.put("ji", ji);
        jo.put("jb", jb);
        assertEquals("""
                    {
                        "jb": true,
                        "ji": 12
                    }""", jo.stringify(true));

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
                    }""", jo.stringify(true));

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
                    ]""", ja.stringify(true));

    }
}
