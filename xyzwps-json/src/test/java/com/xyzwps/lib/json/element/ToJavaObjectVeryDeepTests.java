package com.xyzwps.lib.json.element;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToJavaObjectVeryDeepTests {

    static JsonArray makeVeryDeepJsonArray(int deep) {
        var ja = new JsonArray();
        ja.add(new JsonInteger(1));

        for (int i = 2; i <= deep; i++) {
            var oj = new JsonArray();
            oj.add(new JsonInteger(i));
            oj.add(ja);
            ja = oj;
        }

        return ja;
    }

    static void checkVeryDeepJsonArray(List list, int deep) {
        List a = list;
        for (int i = deep; i >= 1; i--) {
            assertEquals(BigInteger.valueOf(i), a.get(0));
            if (i == 1) {
                assertEquals(1, a.size());
            } else {
                assertEquals(2, a.size());
                a = (List) a.get(1);
            }
        }
    }

    @Test
    void testToJavaObjectVisitor() {
        checkVeryDeepJsonArray((List) makeVeryDeepJsonArray(1).visit(ToJavaObjectVisitor.INSTANCE), 1);
        checkVeryDeepJsonArray((List) makeVeryDeepJsonArray(2).visit(ToJavaObjectVisitor.INSTANCE), 2);
        checkVeryDeepJsonArray((List) makeVeryDeepJsonArray(3).visit(ToJavaObjectVisitor.INSTANCE), 3);

        assertThrows(StackOverflowError.class, () -> makeVeryDeepJsonArray(10_0000).visit(ToJavaObjectVisitor.INSTANCE));
    }

    @Test
    void testToJavaObjectStacker() {
        checkVeryDeepJsonArray((List) (ToJavaObjectStacker.toJavaObject(makeVeryDeepJsonArray(1))), 1);
        checkVeryDeepJsonArray((List) (ToJavaObjectStacker.toJavaObject(makeVeryDeepJsonArray(2))), 2);
        checkVeryDeepJsonArray((List) (ToJavaObjectStacker.toJavaObject(makeVeryDeepJsonArray(3))), 3);

        checkVeryDeepJsonArray((List) (ToJavaObjectStacker.toJavaObject(makeVeryDeepJsonArray(10_0000))), 10_0000);
    }
}
