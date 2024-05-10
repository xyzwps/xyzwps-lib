package com.xyzwps.lib.dollar.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.util.CollectionUtils.*;
import static com.xyzwps.lib.dollar.util.ListFactory.*;

public class CollectionUtilsTests {

    @Test
    void testChunk() {
        String[] cases = new String[]{
                "wont test",
                "[[1], [2], [3], [4], [5], [6]]",
                "[[1, 2], [3, 4], [5, 6]]",
                "[[1, 2, 3], [4, 5, 6]]",
                "[[1, 2, 3, 4], [5, 6]]",
                "[[1, 2, 3, 4, 5], [6]]",
                "[[1, 2, 3, 4, 5, 6]]",
                "[[1, 2, 3, 4, 5, 6]]"
        };
        List<Integer> list = arrayList(1, 2, 3, 4, 5, 6);
        for (int i = 1; i < cases.length; i++) {
            assertEquals(cases[i], chunk(list, i).toString());
        }

        assertThrows(IllegalArgumentException.class, () -> chunk(list, 0));

        {
            List<List<Integer>> chunks = chunk(arrayList(1, 2, 3, 4, 5), 2);
            assertEquals("[[1, 2], [3, 4], [5]]", chunks.toString());
        }
    }


    @Test
    void testCompact() {
        {
            List<Object> list = compact(arrayList("a", "", null));
            assertEquals("[a]", list.toString());
        }
        {
            List<Object> list = compact(arrayList(null, "", false, 0));
            assertEquals("[]", list.toString());
        }
        {
            List<Object> list = compact(arrayList(null, 6, "", "哈哈", false, 0));
            assertEquals("[6, 哈哈]", list.toString());
        }
        {
            List<Object> list = compact(null);
            assertEquals("[]", list.toString());
        }
        {
            List<Object> list = compact(arrayList(null, 1, 0, true, false, "a", ""));
            assertEquals("[1, true, a]", compact(list).toString());
        }
    }


    @Test
    void testConcat() {
        {
            List<Object> list = concat(
                    arrayList("a", "", null),
                    null,
                    arrayList("1", "2"),
                    arrayList(),
                    arrayList(null, "b"));
            assertEquals("[a, , null, 1, 2, null, b]", list.toString());
        }
        {
            List<Object> list = concat();
            assertEquals("[]", list.toString());
        }
        {
            List<Object> list = concat(null, null);
            assertEquals("[]", list.toString());
        }
        {
            List<Integer> list = concat(arrayList(1, 2), arrayList(3, 4));
            assertEquals("[1, 2, 3, 4]", list.toString());
        }
        {
            List<Integer> list = concat(arrayList(1, 2), null, arrayList(3, 4));
            assertEquals("[1, 2, 3, 4]", list.toString());
        }
    }


    @Test
    void testFilter1() {
        {
            List<String> list = filter(arrayList("a", " ", null), Objects::nonNull);
            assertEquals("[a,  ]", list.toString());
        }
        {
            List<Integer> list = filter(arrayList(1, 2, 3, 4, 5), i -> i % 2 == 0);
            assertEquals("[2, 4]", list.toString());
        }
        {
            List<Integer> list = filter(arrayList(1, 2, 3, 4, 5), i -> i % 2 == 1);
            assertEquals("[1, 3, 5]", list.toString());
        }
        {
            List<Integer> list = filter(null, i -> i % 2 == 0);
            assertEquals("[]", list.toString());
        }
        assertThrows(NullPointerException.class, () -> filter(arrayList(1, 2, 3, 4, 5), (Predicate<Integer>) null).toString());
    }


    @Test
    void testFilter2() {
        {
            List<Integer> list = filter(arrayList(1, 2, 3, 4, 5), (it, i) -> i % 2 == 0);
            assertEquals("[1, 3, 5]", list.toString());
        }
        {
            List<Integer> list = filter(null, (e, i) -> i % 2 == 0);
            assertEquals("[]", list.toString());
        }
        assertThrows(NullPointerException.class, () -> filter(arrayList(1, 2, 3, 4, 5), (BiPredicate<Integer, Integer>) null).toString());
    }


    @Test
    void testFirst() {
        assertEquals(Optional.of(1), first(arrayList(1, 2)));
        assertEquals(Optional.empty(), first(arrayList(null, 2)));
    }


    @SuppressWarnings("ConstantValue")
    @Test
    void testIsEmpty() {
        assertTrue(isEmpty(null));
        assertTrue(isEmpty(arrayList()));

        assertFalse(isNotEmpty(null));
        assertFalse(isNotEmpty(arrayList()));
    }

}
