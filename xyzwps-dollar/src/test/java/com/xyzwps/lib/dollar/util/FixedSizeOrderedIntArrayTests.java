package com.xyzwps.lib.dollar.util;

import com.xyzwps.lib.dollar.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedSizeOrderedIntArrayTests {

    @Test
    void asc() {
        var array = new FixedSizeOrderedIntArray(7, Direction.ASC);

        assertEquals("[]", array.toList().toString());

        array.add(1);
        assertEquals("[1]", array.toList().toString());

        array.add(100);
        assertEquals("[1, 100]", array.toList().toString());

        array.add(-3);
        assertEquals("[-3, 1, 100]", array.toList().toString());

        array.add(8);
        assertEquals("[-3, 1, 8, 100]", array.toList().toString());

        array.add(5);
        assertEquals("[-3, 1, 5, 8, 100]", array.toList().toString());

        array.add(-4);
        assertEquals("[-4, -3, 1, 5, 8, 100]", array.toList().toString());

        array.add(101);
        assertEquals("[-4, -3, 1, 5, 8, 100, 101]", array.toList().toString());

        array.add(102);
        assertEquals("[-4, -3, 1, 5, 8, 100, 101]", array.toList().toString());

        array.add(100);
        assertEquals("[-4, -3, 1, 5, 8, 100, 100]", array.toList().toString());

        array.add(99);
        assertEquals("[-4, -3, 1, 5, 8, 99, 100]", array.toList().toString());

        array.add(80);
        assertEquals("[-4, -3, 1, 5, 8, 80, 99]", array.toList().toString());

        array.add(7);
        assertEquals("[-4, -3, 1, 5, 7, 8, 80]", array.toList().toString());

        array.add(4);
        assertEquals("[-4, -3, 1, 4, 5, 7, 8]", array.toList().toString());

        array.add(0);
        assertEquals("[-4, -3, 0, 1, 4, 5, 7]", array.toList().toString());

        array.add(-1);
        assertEquals("[-4, -3, -1, 0, 1, 4, 5]", array.toList().toString());

        array.add(-5);
        assertEquals("[-5, -4, -3, -1, 0, 1, 4]", array.toList().toString());
    }

    @Test
    void desc() {
        var array = new FixedSizeOrderedIntArray(7, Direction.DESC);

        assertEquals("[]", array.toList().toString());

        array.add(1);
        assertEquals("[1]", array.toList().toString());

        array.add(100);
        assertEquals("[100, 1]", array.toList().toString());

        array.add(-3);
        assertEquals("[100, 1, -3]", array.toList().toString());

        array.add(8);
        assertEquals("[100, 8, 1, -3]", array.toList().toString());

        array.add(5);
        assertEquals("[100, 8, 5, 1, -3]", array.toList().toString());

        array.add(-4);
        assertEquals("[100, 8, 5, 1, -3, -4]", array.toList().toString());

        array.add(101);
        assertEquals("[101, 100, 8, 5, 1, -3, -4]", array.toList().toString());

        array.add(102);
        assertEquals("[102, 101, 100, 8, 5, 1, -3]", array.toList().toString());

        array.add(100);
        assertEquals("[102, 101, 100, 100, 8, 5, 1]", array.toList().toString());

        array.add(99);
        assertEquals("[102, 101, 100, 100, 99, 8, 5]", array.toList().toString());

        array.add(80);
        assertEquals("[102, 101, 100, 100, 99, 80, 8]", array.toList().toString());

        array.add(70);
        assertEquals("[102, 101, 100, 100, 99, 80, 70]", array.toList().toString());

        array.add(94);
        assertEquals("[102, 101, 100, 100, 99, 94, 80]", array.toList().toString());

        array.add(0);
        assertEquals("[102, 101, 100, 100, 99, 94, 80]", array.toList().toString());

        array.add(-1);
        assertEquals("[102, 101, 100, 100, 99, 94, 80]", array.toList().toString());

        array.add(-5);
        assertEquals("[102, 101, 100, 100, 99, 94, 80]", array.toList().toString());
    }
}
