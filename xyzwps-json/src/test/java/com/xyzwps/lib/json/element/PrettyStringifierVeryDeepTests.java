package com.xyzwps.lib.json.element;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrettyStringifierVeryDeepTests {

    static JsonElement createVeryDeepJson(int deep) {
        var ja = new JsonArray();
        for (int i = 2; i <= deep; i++) {
            var outerJa = new JsonArray();
            outerJa.add(ja);
            ja = outerJa;
        }
        return ja;
    }

    static String createExpectedString(int deep) {
        if (deep == 1) {
            return "[]";
        }

        var sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 2; i < deep; i++) {
            sb.append("    ".repeat(Math.max(0, i - 1)));
            sb.append("[\n");
        }
        sb.append("    ".repeat(Math.max(0, deep - 1)));
        sb.append("[]\n");

        for (int i = deep - 1; i >= 2; i--) {
            sb.append("    ".repeat(Math.max(0, i - 1)));
            sb.append("]\n");
        }
        return sb.append(']').toString();
    }


    @Test
    void testPrettyStringifierVisitor() {
        assertEquals("[]", createVeryDeepJson(1).visit(0, new PrettyStringifierVisitor()).toString());
        assertEquals("""
                [
                    []
                ]""", createVeryDeepJson(2).visit(0, new PrettyStringifierVisitor()).toString());
        assertEquals("""
                [
                    [
                        []
                    ]
                ]""", createVeryDeepJson(3).visit(0, new PrettyStringifierVisitor()).toString());
        assertEquals(createExpectedString(1), createVeryDeepJson(1).visit(0, new PrettyStringifierVisitor()).toString());
        assertEquals(createExpectedString(2), createVeryDeepJson(2).visit(0, new PrettyStringifierVisitor()).toString());
        assertEquals(createExpectedString(3), createVeryDeepJson(3).visit(0, new PrettyStringifierVisitor()).toString());

        assertThrows(StackOverflowError.class, () -> createVeryDeepJson(5000).visit(0, new PrettyStringifierVisitor()).toString());
    }

    @Test
    void testPrettyStringifierStacker() {
        assertEquals("[]", PrettyStringifierStacker.stringify(createVeryDeepJson(1)));
        assertEquals("""
                [
                    []
                ]""", PrettyStringifierStacker.stringify(createVeryDeepJson(2)));
        assertEquals("""
                [
                    [
                        []
                    ]
                ]""", PrettyStringifierStacker.stringify(createVeryDeepJson(3)));
        assertEquals(createExpectedString(1), PrettyStringifierStacker.stringify(createVeryDeepJson(1)));
        assertEquals(createExpectedString(2), PrettyStringifierStacker.stringify(createVeryDeepJson(2)));
        assertEquals(createExpectedString(3), PrettyStringifierStacker.stringify(createVeryDeepJson(3)));

        assertEquals(createExpectedString(5000), PrettyStringifierStacker.stringify(createVeryDeepJson(5000)));

    }

}
