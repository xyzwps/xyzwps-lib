package com.xyzwps.lib.dollar.foreach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntSeqTests {

    @Test
    void filter() {
        assertEquals("[1, 3, 5]", IntSeq.range(1, 6).filter(i -> i % 2 == 1).toList().toString());
        assertEquals("[2, 4]", IntSeq.range(1, 6).filter(i -> i % 2 == 0).toList().toString());
        assertEquals("[]", IntSeq.range(1, 6).filter(i -> i > 100).toList().toString());

        assertThrows(NullPointerException.class, () -> IntSeq.range(1, 6).filter(null).avg());
    }

    @Test
    void sum() {
        assertEquals(15, IntSeq.range(1, 6).sum());
        assertEquals(22, IntSeq.just(3, 7, 8, 4).sum());
    }

    @Test
    void min() {
        assertEquals(1, IntSeq.range(1, 6).min());
        assertEquals(2, IntSeq.just(3, 7, 2, 8, 4).min());
    }

    @Test
    void min_n() {
        assertEquals("[1]", IntSeq.range(1, 6).min(1).toString());
        assertEquals("[1, 2]", IntSeq.range(1, 6).min(2).toString());
        assertEquals("[1, 2, 3]", IntSeq.range(1, 6).min(3).toString());
        assertEquals("[1, 2, 3, 4]", IntSeq.range(1, 6).min(4).toString());
        assertEquals("[1, 2, 3, 4, 5]", IntSeq.range(1, 6).min(5).toString());
        assertEquals("[1, 2, 3, 4, 5]", IntSeq.range(1, 6).min(6).toString());

        assertEquals("[2]", IntSeq.just(3, 7, 2, 8, 4).min(1).toString());
        assertEquals("[2, 3]", IntSeq.just(3, 7, 2, 8, 4).min(2).toString());
        assertEquals("[2, 3, 4]", IntSeq.just(3, 7, 2, 8, 4).min(3).toString());
        assertEquals("[2, 3, 4, 7]", IntSeq.just(3, 7, 2, 8, 4).min(4).toString());
        assertEquals("[2, 3, 4, 7, 8]", IntSeq.just(3, 7, 2, 8, 4).min(5).toString());
        assertEquals("[2, 3, 4, 7, 8]", IntSeq.just(3, 7, 2, 8, 4).min(6).toString());

        for (int i = -100; i < 1; i++) {
            final int n = i;
            assertThrows(IllegalArgumentException.class, () -> IntSeq.range(1, 6).min(n));
        }
    }


    @Test
    void max() {
        assertEquals(5, IntSeq.range(1, 6).max());
        assertEquals(8, IntSeq.just(3, 7, 2, 8, 4).max());
    }

    @Test
    void max_n() {
        assertEquals("[5]", IntSeq.range(1, 6).max(1).toString());
        assertEquals("[5, 4]", IntSeq.range(1, 6).max(2).toString());
        assertEquals("[5, 4, 3]", IntSeq.range(1, 6).max(3).toString());
        assertEquals("[5, 4, 3, 2]", IntSeq.range(1, 6).max(4).toString());
        assertEquals("[5, 4, 3, 2, 1]", IntSeq.range(1, 6).max(5).toString());
        assertEquals("[5, 4, 3, 2, 1]", IntSeq.range(1, 6).max(6).toString());

        assertEquals("[8]", IntSeq.just(3, 7, 2, 8, 4).max(1).toString());
        assertEquals("[8, 7]", IntSeq.just(3, 7, 2, 8, 4).max(2).toString());
        assertEquals("[8, 7, 4]", IntSeq.just(3, 7, 2, 8, 4).max(3).toString());
        assertEquals("[8, 7, 4, 3]", IntSeq.just(3, 7, 2, 8, 4).max(4).toString());
        assertEquals("[8, 7, 4, 3, 2]", IntSeq.just(3, 7, 2, 8, 4).max(5).toString());
        assertEquals("[8, 7, 4, 3, 2]", IntSeq.just(3, 7, 2, 8, 4).max(6).toString());

        for (int i = -100; i < 1; i++) {
            final int n = i;
            assertThrows(IllegalArgumentException.class, () -> IntSeq.range(1, 6).max(n));
        }
    }


    @Test
    void avg() {
        assertEquals(3, IntSeq.range(1, 6).avg());
        assertEquals(4.8, IntSeq.just(3, 7, 2, 8, 4).avg());
    }
}
