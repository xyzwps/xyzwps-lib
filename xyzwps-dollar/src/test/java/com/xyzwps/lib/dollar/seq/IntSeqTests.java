package com.xyzwps.lib.dollar.seq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntSeqTests {

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
    void max() {
        assertEquals(5, IntSeq.range(1, 6).max());
        assertEquals(8, IntSeq.just(3, 7, 2, 8, 4).max());
    }

    @Test
    void avg() {
        assertEquals(3, IntSeq.range(1, 6).avg());
        assertEquals(4.8, IntSeq.just(3, 7, 2, 8, 4).avg());
    }
}
