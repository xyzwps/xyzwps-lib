package com.xyzwps.lib.dollar.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static com.xyzwps.lib.dollar.util.ObjectUtils.*;

public class ObjectUtilsTests {

    @Test
    void testDefaultTo() {
        assertEquals(1, defaultTo(null, 1));
        assertEquals(2, defaultTo(2, 1));
        //noinspection ConstantValue
        assertNull(defaultTo(null, null));
    }

}
