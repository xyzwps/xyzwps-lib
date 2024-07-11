package com.xyzwps.lib.dollar.util;

import com.xyzwps.lib.dollar.Pair;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.xyzwps.lib.dollar.Direction.ASC;
import static com.xyzwps.lib.dollar.Direction.DESC;
import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilsTests implements CollectionUtils {

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
        List<Integer> list = $.arrayList(1, 2, 3, 4, 5, 6);
        for (int i = 1; i < cases.length; i++) {
            assertEquals(cases[i], chunk(list, i).toString());
        }

        assertThrows(IllegalArgumentException.class, () -> chunk(list, 0));

        {
            List<List<Integer>> chunks = chunk($.arrayList(1, 2, 3, 4, 5), 2);
            assertEquals("[[1, 2], [3, 4], [5]]", chunks.toString());
        }
    }


    @Test
    void testCompact() {
        {
            List<Object> list = compact($.arrayList("a", "", null));
            assertEquals("[a]", list.toString());
        }
        {
            List<Object> list = compact($.arrayList(null, "", false, 0));
            assertEquals("[]", list.toString());
        }
        {
            List<Object> list = compact($.arrayList(null, 6, "", "哈哈", false, 0));
            assertEquals("[6, 哈哈]", list.toString());
        }
        {
            List<Object> list = compact(null);
            assertEquals("[]", list.toString());
        }
        {
            List<Object> list = compact($.arrayList(null, 1, 0, true, false, "a", ""));
            assertEquals("[1, true, a]", compact(list).toString());
        }
    }


    @Test
    void testConcat() {
        {
            List<Object> list = concat(
                    $.arrayList("a", "", null),
                    null,
                    $.arrayList("1", "2"),
                    $.arrayList(),
                    $.arrayList(null, "b"));
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
            List<Integer> list = concat($.arrayList(1, 2), $.arrayList(3, 4));
            assertEquals("[1, 2, 3, 4]", list.toString());
        }
        {
            List<Integer> list = concat($.arrayList(1, 2), null, $.arrayList(3, 4));
            assertEquals("[1, 2, 3, 4]", list.toString());
        }
    }


    @Test
    void testFilter1() {
        {
            List<String> list = filter($.arrayList("a", " ", null), Objects::nonNull);
            assertEquals("[a,  ]", list.toString());
        }
        {
            List<Integer> list = filter($.arrayList(1, 2, 3, 4, 5), i -> i % 2 == 0);
            assertEquals("[2, 4]", list.toString());
        }
        {
            List<Integer> list = filter($.arrayList(1, 2, 3, 4, 5), i -> i % 2 == 1);
            assertEquals("[1, 3, 5]", list.toString());
        }
        {
            List<Integer> list = filter(null, i -> i % 2 == 0);
            assertEquals("[]", list.toString());
        }
        assertThrows(NullPointerException.class, () -> filter($.arrayList(1, 2, 3, 4, 5), (Predicate<Integer>) null).toString());
    }


    @Test
    void testFilter2() {
        {
            List<Integer> list = filter($.arrayList(1, 2, 3, 4, 5), (it, i) -> i % 2 == 0);
            assertEquals("[1, 3, 5]", list.toString());
        }
        {
            List<Integer> list = filter(null, (e, i) -> i % 2 == 0);
            assertEquals("[]", list.toString());
        }
        assertThrows(NullPointerException.class, () -> filter($.arrayList(1, 2, 3, 4, 5), (BiPredicate<Integer, Integer>) null).toString());
    }


    @Test
    void testFirst() {
        assertEquals(Optional.of(1), first($.arrayList(1, 2)));
        assertEquals(Optional.empty(), first($.arrayList(null, 2)));
    }

    @Test
    void testFlatMap() {
        {
            List<Integer> list = flatMap($.arrayList(1, 2), i -> $.arrayList(i * 10 + 1, i * 10 + 2));
            assertEquals("[11, 12, 21, 22]", list.toString());
        }
        {
            List<Integer> list = flatMap($.arrayList(1, 2, 3), i -> $.arrayList(i * 2, i * 3));
            assertEquals("[2, 3, 4, 6, 6, 9]", list.toString());
        }

    }

    @Test
    void testForEach1() {
        List<Integer> t = new ArrayList<>();
        forEach($.arrayList(1, 2, 3), i -> t.add(i));
        assertEquals("[1, 2, 3]", t.toString());
    }

    @Test
    void testForEach2() {
        List<Integer> t = new ArrayList<>();
        forEach($.arrayList(1, 2, 3), (it, index) -> t.add(it + (index + 1) * 10));
        assertEquals("[11, 22, 33]", t.toString());
    }

    @Test
    void testGroupBy() {
        {
            Map<Integer, List<Integer>> map = groupBy($.arrayList(1, 4, 7, 2, 5, 3), i -> i % 3);
            assertEquals(3, map.size());
            assertEquals("[1, 4, 7]", map.get(1).toString());
            assertEquals("[2, 5]", map.get(2).toString());
            assertEquals("[3]", map.get(0).toString());
        }

        {
            Map<String, List<Integer>> map = groupBy($.arrayList(1, 2, 3, 4, 5), i -> i % 2 == 0 ? "even" : "odd");
            assertEquals(2, map.size());
            assertEquals("[1, 3, 5]", map.get("odd").toString());
            assertEquals("[2, 4]", map.get("even").toString());
        }
    }


    @SuppressWarnings("ConstantValue")
    @Test
    void testIsEmpty() {
        assertTrue(isEmpty(null));
        assertTrue(isEmpty($.arrayList()));

        assertFalse(isNotEmpty(null));
        assertFalse(isNotEmpty($.arrayList()));
    }

    @Test
    void testKeyBy() {
        {
            Map<Integer, Integer> map = keyBy($.arrayList(1, 4, 7, 2, 5, 3), i -> i % 3);
            assertEquals(3, map.size());
            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(0));
        }
        {
            Map<String, Integer> map = keyBy($.arrayList(1, 2, 3, 4, 5), i -> i % 2 == 0 ? "even" : "odd");
            assertEquals(2, map.size());
            assertEquals(1, map.get("odd"));
            assertEquals(2, map.get("even"));
        }
    }

    @Test
    void testLast() {
        assertTrue(last(null).isEmpty());

        assertEquals(last($.arrayList(1, 2, 3)).orElse(-1), 3);

        assertTrue(last($.arrayList(1, 2, null)).isEmpty());
    }

    @Test
    void testMap1() {
        assertThrows(NullPointerException.class, () -> map($.arrayList(1, 2, 3), (Function<Integer, Object>) null));

        {
            List<Integer> list = map($.arrayList(1, 2, 3), i -> i * 2);
            assertEquals("[2, 4, 6]", list.toString());
        }
        {
            List<Integer> list = map($.arrayList(1, 2, 3), i -> i % 2);
            assertEquals("[1, 0, 1]", list.toString());
        }
    }


    @Test
    void testMap2() {
        assertThrows(NullPointerException.class, () -> map($.arrayList(1, 2, 3), (ObjIntFunction<Integer, Object>) null));

        {
            List<Integer> list = map($.arrayList(1, 2, 3), (it, i) -> it + 10 * (i + 1));
            assertEquals("[11, 22, 33]", list.toString());
        }
    }


    @Test
    void testOrderBy() {
        {
            List<Integer> list = orderBy($.arrayList(1, 3, 5, 2, 4), Function.identity(), ASC);
            assertEquals("[1, 2, 3, 4, 5]", list.toString());
        }
        {
            List<Integer> list = orderBy($.arrayList(1, 3, 5, 2, 4), Function.identity(), DESC);
            assertEquals("[5, 4, 3, 2, 1]", list.toString());
        }
        {
            List<Integer> list = orderBy((List<Integer>) null, Function.identity(), DESC);
            assertEquals("[]", list.toString());
        }
        {
            List<String> list = orderBy($.arrayList("C1", "A2", "B3"), it -> Integer.parseInt(it.substring(1)), ASC);
            assertEquals("[C1, A2, B3]", list.toString());
        }
        {
            List<String> list = orderBy($.arrayList("C1", "A2", "B3"), Function.identity(), ASC);
            assertEquals("[A2, B3, C1]", list.toString());
        }
    }


    @Test
    void testReduce() {
        assertThrows(NullPointerException.class, () -> reduce($.arrayList(1), 1, null));
        assertEquals(20, reduce($.arrayList(1, 2, 3, 4), 10, Integer::sum));
    }

    @Test
    void testReverse() {
        {
            List<Integer> list = reverse($.arrayList(1, 2, 3));
            assertEquals("[3, 2, 1]", list.toString());
        }
        {
            List<Integer> list = reverse($.arrayList(1, 2, 3, 4));
            assertEquals("[4, 3, 2, 1]", list.toString());
        }
    }


    @Test
    void testSize() {
        assertEquals(0, size(null));
        assertEquals(0, size($.arrayList()));
        assertEquals(1, size($.arrayList(1)));
    }

    @Test
    void testTake() {
        {
            List<Integer> list = take($.arrayList(1, 2, 3, 4), 2);
            assertEquals("[1, 2]", list.toString());
        }
        {
            List<Integer> list = take($.arrayList(1, 2, 3, 4, 5), 6);
            assertEquals("[1, 2, 3, 4, 5]", list.toString());
        }
        {
            List<Integer> list = take($.arrayList(1, 2, 3, 4, 5), 3);
            assertEquals("[1, 2, 3]", list.toString());
        }
    }

    @Test
    void testTakeWhile() {
        assertThrows(NullPointerException.class, () -> takeWhile($.arrayList(1, 2, 3, 4, 5), null));

        {
            List<Integer> list = takeWhile($.arrayList(1, 2, 3, 4, 5), i -> i < 3);
            assertEquals("[1, 2]", list.toString());
        }
    }

    @Test
    void testToSet() {
        {
            Set<Integer> set = toSet(null);
            assertEquals(0, set.size());
        }
        {
            Set<Integer> set = toSet($.arrayList(1, 2, 1, 3, 4));
            assertEquals(4, set.size());
            assertTrue(set.contains(1));
            assertTrue(set.contains(2));
            assertTrue(set.contains(3));
            assertTrue(set.contains(4));
        }
    }

    @Test
    void testUnique() {
        {
            List<Integer> list = unique($.arrayList(1, 2, 1, 3));
            assertEquals("[1, 2, 3]", list.toString());
        }
        {
            List<Integer> list = unique($.arrayList(1, 2, 1));
            assertEquals("[1, 2]", list.toString());
        }
    }

    @Test
    void testUniqueBy() {
        assertThrows(NullPointerException.class, () -> uniqueBy($.arrayList(1, 2, 1, 3, 4), (Function<Integer, Object>) null));

        {
            List<Integer> list = uniqueBy($.arrayList(1, 2, 1, 3, 4), i -> i % 3);
            assertEquals("[1, 2, 3]", list.toString());
        }
        {
            List<Double> list = uniqueBy($.arrayList(1.2, 2.3, 1.4), Double::intValue);
            assertEquals("[1.2, 2.3]", list.toString());
        }
    }


    @Test
    void uniqueBy_withIndex() {
        assertThrows(NullPointerException.class, () -> uniqueBy($.arrayList(1, 2, 1, 3, 4), (ObjIntFunction<Integer, Object>) null));

        {
            List<Integer> list = uniqueBy($.arrayList(1, 2, 1, 3, 4), (it, i) -> (it + i) % 3);
            assertEquals("[1, 2, 4]", list.toString());
        }
    }

    @Test
    void testZip() {
        {
            List<Pair<Integer, Integer>> list = zip($.arrayList(1, 2, 3), $.arrayList(1, 2));
            assertEquals("[(1, 1), (2, 2), (3, null)]", list.toString());
        }
        {
            List<Pair<Integer, Integer>> list = zip($.arrayList(1, 2, 3), $.arrayList(1, 2, 3));
            assertEquals("[(1, 1), (2, 2), (3, 3)]", list.toString());
        }
        {
            List<Pair<Integer, Integer>> list = zip($.arrayList(1, 2, 3), $.arrayList(1, 2, 3, 4, 5));
            assertEquals("[(1, 1), (2, 2), (3, 3), (null, 4), (null, 5)]", list.toString());
        }
        {
            List<Pair<Integer, Integer>> list = zip($.arrayList(1, 2, 3), $.arrayList());
            assertEquals("[(1, null), (2, null), (3, null)]", list.toString());
        }
        {
            List<Pair<Integer, Integer>> list = zip($.arrayList(1, 2, 3), null);
            assertEquals("[(1, null), (2, null), (3, null)]", list.toString());
        }
        {
            List<Integer> list = zip($.arrayList(1, 2, 3), $.arrayList(1, 2), (l, r) -> (l == null ? 0 : l) + (r == null ? 0 : r));
            assertEquals("[2, 4, 3]", list.toString());
        }

        assertThrows(NullPointerException.class, () -> zip($.arrayList(1), $.arrayList(2), null));

        {
            List<Pair<Object, Object>> list = zip(null, null);
            assertEquals(list.size(), 0);
        }
    }


}
