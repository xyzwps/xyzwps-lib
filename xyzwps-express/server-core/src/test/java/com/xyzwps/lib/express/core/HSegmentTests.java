package com.xyzwps.lib.express.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.express.core.HSegment.*;

class HSegmentTests {

    static final Class<IllegalArgumentException> IAE = IllegalArgumentException.class;

    @Test
    void create() {
        assertEquals(StarSegment.INSTANCE, from("*"));
        assertEquals(Star2Segment.INSTANCE, from("**"));
        assertEquals(new VariableSegment("abc"), from("{abc}"));
        assertEquals(new PlainSegment("abc"), from("abc"));
        assertEquals(new PlainSegment("aB_1"), from("aB_1"));

        assertEquals("Segment string cannot be empty", assertThrows(IAE, () -> from("")).getMessage());
        assertEquals("Segment string cannot be empty", assertThrows(IAE, () -> from(null)).getMessage());

        assertEquals("Invalid path variable segment '{'", assertThrows(IAE, () -> from("{")).getMessage());
        assertEquals("Invalid path variable segment '{ddd'", assertThrows(IAE, () -> from("{ddd")).getMessage());

        assertEquals("Invalid path variable segment '{}'", assertThrows(IAE, () -> from("{}")).getMessage());
        assertEquals("Invalid path variable name ' '", assertThrows(IAE, () -> from("{ }")).getMessage());
        assertEquals("Invalid path variable name '*'", assertThrows(IAE, () -> from("{*}")).getMessage());

        assertEquals("Invalid segment 'a*'", assertThrows(IAE, () -> from("a*")).getMessage());

        assertEquals("Invalid path segment 'a?'", assertThrows(IAE, () -> from("a?")).getMessage());
    }
}
