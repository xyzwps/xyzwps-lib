package com.xyzwps.lib.json.element;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompactStringifierVeryDeepTests {

    private static JsonArray makeJsonArray(int deep) {
        var ja = new JsonArray();
        ja.add(new JsonInteger(1));

        for (int i = 2; i <= deep; i++) {
            var outJa = new JsonArray();
            outJa.add(ja);
            ja = outJa;
        }
        return ja;
    }

    private static String makeJsonArrayString(int deep) {
        return "[".repeat(Math.max(0, deep)) +
               '1' +
               "]".repeat(Math.max(0, deep));
    }

    @Test
    void testToJsonStringVisitor() {
        assertEquals("[1]", makeJsonArray(1).visit(CompactStringifierVisitor.INSTANCE));
        assertEquals("[[1]]", makeJsonArray(2).visit(CompactStringifierVisitor.INSTANCE));
        assertEquals(makeJsonArrayString(1), makeJsonArray(1).visit(CompactStringifierVisitor.INSTANCE));
        assertEquals(makeJsonArrayString(2), makeJsonArray(2).visit(CompactStringifierVisitor.INSTANCE));

        assertThrows(StackOverflowError.class, () -> makeJsonArray(10_0000).visit(CompactStringifierVisitor.INSTANCE));
    }

    @Test
    void testStackToJsonString() {
        assertEquals("[1]", CompactStringifierStacker.stringify(makeJsonArray(1)));
        assertEquals("[[1]]", CompactStringifierStacker.stringify(makeJsonArray(2)));
        assertEquals(makeJsonArrayString(1), CompactStringifierStacker.stringify(makeJsonArray(1)));
        assertEquals(makeJsonArrayString(2), CompactStringifierStacker.stringify(makeJsonArray(2)));

        assertEquals(makeJsonArrayString(10_0000), CompactStringifierStacker.stringify(makeJsonArray(10_0000)));
    }

}
