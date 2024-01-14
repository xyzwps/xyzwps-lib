package com.xyzwps.lib.dollar.foreach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.foreach.ISeq.*;

class ISeqTests {

    @Test
    void test() {
        final ISeq<Integer> seq0 = null;
        assertEquals("()", string(seq0));
        assertNull(first(seq0));
        assertNull(rest(seq0));
        assertNull(next(seq0));

        final ISeq<Integer> seq1 = cons(1, seq0);
        assertEquals("(1)", string(seq1));
        assertEquals(1, first(seq1));
        assertEquals("()", string(rest(seq1)));
        assertNull(next(seq1));

        final ISeq<Integer> seq2 = cons(2, seq1);
        assertEquals("(2 1)", string(seq2));
        assertEquals(2, first(seq2));
        assertEquals("(1)", string(rest(seq2)));
        assertEquals(1, next(seq2));

        final ISeq<Integer> seq3 = cons(3, seq2);
        assertEquals("(3 2 1)", string(seq3));
        assertEquals(3, first(seq3));
        assertEquals("(2 1)", string(rest(seq3)));
        assertEquals(2, next(seq3));
    }

    @Test
    void testConj() {
        assertEquals("()", string(conj(null)));
        assertEquals("(1)", string(conj(null, 1)));
        assertEquals("(2 1)", string(conj(null, 1, 2)));
        assertEquals("(3 2 1)", string(conj(null, 1, 2, 3)));

        ISeq<Integer> seq = cons(1, cons(2, cons(3, null)));
        assertEquals("(1 2 3)", string(conj(seq)));
        assertEquals("(1 1 2 3)", string(conj(seq, 1)));
        assertEquals("(2 1 1 2 3)", string(conj(seq, 1, 2)));
        assertEquals("(3 2 1 1 2 3)", string(conj(seq, 1, 2, 3)));
    }

    @Test
    void testInto() {
        assertEquals("()", string(into(null, null)));
        assertEquals("(1)", string(into(null, cons(1, null))));
        assertEquals("(1 2)", string(into(null, cons(2, cons(1, null)))));
        assertEquals("(1 2 3)", string(into(null, cons(3, cons(2, cons(1, null))))));

        ISeq<Integer> seq = cons(1, cons(2, cons(3, null)));
        assertEquals("(1 2 3)", string(into(seq, null)));
        assertEquals("(1 1 2 3)", string(into(seq, cons(1, null))));
        assertEquals("(1 2 1 2 3)", string(into(seq, cons(2, cons(1, null)))));
        assertEquals("(1 2 3 1 2 3)", string(into(seq, cons(3, cons(2, cons(1, null))))));
    }
}
