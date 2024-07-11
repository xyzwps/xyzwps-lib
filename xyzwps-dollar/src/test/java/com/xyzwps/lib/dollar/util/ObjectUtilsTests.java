package com.xyzwps.lib.dollar.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static com.xyzwps.lib.dollar.Dollar.$.*;

public class ObjectUtilsTests {

    @Test
    void testDefaultTo() {
        assertEquals(1, defaultTo(null, 1));
        assertEquals(2, defaultTo(2, 1));
        assertNull(defaultTo(null, null));
    }

    @Test
    void testIsFalsey() {
        assertTrue(isFalsey(null));
        assertTrue(isFalsey(false));
        assertTrue(isFalsey(""));
        assertTrue(isFalsey(0));
        assertTrue(isFalsey(0L));
        assertTrue(isFalsey(0.0F));
        assertTrue(isFalsey(0.0));
    }

}
