package com.xyzwps.lib.dollar;


import com.xyzwps.lib.dollar.util.Range;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantValue")
class RangeTests {

    @Test
    void cornerCases() {
        assertThrows(IllegalArgumentException.class, () -> new Range(1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Range(1, 0));
    }

    @Test
    void iterator() {
        Range range = new Range(1, 5);

        // common
        {
            Iterator<Integer> itr = range.iterator();

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(1, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(2, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(3, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(4, itr.next());

            assertFalse(itr.hasNext());
        }

        // just next
        {
            Iterator<Integer> itr = range.iterator();

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertEquals(4, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }


    @Test
    void forEach() {
        Range range = new Range(-5, 10);
        int sum = 0;
        for (int i : range) {
            sum += i;
        }
        assertEquals(30, sum);
    }
}
