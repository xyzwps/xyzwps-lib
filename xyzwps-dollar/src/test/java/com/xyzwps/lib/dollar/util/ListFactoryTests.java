package com.xyzwps.lib.dollar.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListFactoryTests implements ListFactory {

    @Test
    void testArrayList() {
        {
            List<Integer> list = arrayList();
            assertEquals("[]", list.toString());
        }
        {
            List<Integer> list = arrayList(1);
            assertEquals("[1]", list.toString());
        }
        {
            List<Integer> list = arrayList(1, 2);
            assertEquals("[1, 2]", list.toString());
        }
        {
            List<Integer> list = arrayList(1, 2, 3);
            assertEquals("[1, 2, 3]", list.toString());
        }
        {
            List<Integer> list = arrayList(1, 2, 3, 4);
            assertEquals("[1, 2, 3, 4]", list.toString());
        }
        {
            List<Integer> list = arrayList(1, 2, 3, 4, 5);
            assertEquals("[1, 2, 3, 4, 5]", list.toString());
        }
    }

    @Test
    void testListFromIterator() {
        {
            List<Integer> list = arrayListFrom(null);
            assertEquals(0, list.size());
        }
        {
            List<Integer> list = arrayListFrom(arrayList(1, 2, 3).iterator());
            assertEquals("[1, 2, 3]", list.toString());
        }
    }

}
