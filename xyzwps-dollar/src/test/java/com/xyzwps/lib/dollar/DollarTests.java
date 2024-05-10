package com.xyzwps.lib.dollar;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import com.xyzwps.lib.dollar.util.ObjIntFunction;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;
import static com.xyzwps.lib.dollar.Direction.*;

class DollarTests {

    @Test
    void zip() {
        {
            List<Pair<Integer, Integer>> list = $.zip($.arrayList(1, 2, 3), $.arrayList(1, 2));
            assertEquals("[(1, 1), (2, 2), (3, null)]", list.toString());
        }
        {
            List<Pair<Integer, Integer>> list = $.zip($.arrayList(1, 2, 3), $.arrayList(1, 2, 3));
            assertEquals("[(1, 1), (2, 2), (3, 3)]", list.toString());
        }
        {
            List<Pair<Integer, Integer>> list = $.zip($.arrayList(1, 2, 3), $.arrayList(1, 2, 3, 4, 5));
            assertEquals("[(1, 1), (2, 2), (3, 3), (null, 4), (null, 5)]", list.toString());
        }
        {
            List<Pair<Integer, Integer>> list = $.zip($.arrayList(1, 2, 3), $.arrayList());
            assertEquals("[(1, null), (2, null), (3, null)]", list.toString());
        }
        {
            List<Pair<Integer, Integer>> list = $.zip($.arrayList(1, 2, 3), null);
            assertEquals("[(1, null), (2, null), (3, null)]", list.toString());
        }
        {
            List<Integer> list = $.zip($.arrayList(1, 2, 3), $.arrayList(1, 2), (l, r) -> (l == null ? 0 : l) + (r == null ? 0 : r));
            assertEquals("[2, 4, 3]", list.toString());
        }

        assertThrows(NullPointerException.class, () -> $.zip($.arrayList(1), $.arrayList(2), null));

        {
            List<Pair<Object, Object>> list = $.zip(null, null);
            assertEquals(list.size(), 0);
        }
    }

    @Test
    void unique() {
        {
            List<Integer> list = $.unique($.arrayList(1, 2, 1, 3));
            assertEquals("[1, 2, 3]", list.toString());
        }
        {
            List<Integer> list = $.unique($.arrayList(1, 2, 1));
            assertEquals("[1, 2]", list.toString());
        }
    }

    @Test
    void uniqueBy() {
        assertThrows(NullPointerException.class, () -> $.uniqueBy($.arrayList(1, 2, 1, 3, 4), (Function<Integer, Object>) null));

        {
            List<Integer> list = $.uniqueBy($.arrayList(1, 2, 1, 3, 4), i -> i % 3);
            assertEquals("[1, 2, 3]", list.toString());
        }
        {
            List<Double> list = $.uniqueBy($.arrayList(1.2, 2.3, 1.4), Double::intValue);
            assertEquals("[1.2, 2.3]", list.toString());
        }
    }

    @Test
    void uniqueBy_withIndex() {
        assertThrows(NullPointerException.class, () -> $.uniqueBy($.arrayList(1, 2, 1, 3, 4), (ObjIntFunction<Integer, Object>) null));

        {
            List<Integer> list = $.uniqueBy($.arrayList(1, 2, 1, 3, 4), (it, i) -> (it + i) % 3);
            assertEquals("[1, 2, 4]", list.toString());
        }
    }

    @Test
    void toSet() {
        {
            Set<Integer> set = $.toSet(null);
            assertEquals(0, set.size());
        }
        {
            Set<Integer> set = $.toSet($.arrayList(1, 2, 1, 3, 4));
            assertEquals(4, set.size());
            assertTrue(set.contains(1));
            assertTrue(set.contains(2));
            assertTrue(set.contains(3));
            assertTrue(set.contains(4));
        }
    }

    @Test
    void takeWhile() {
        assertThrows(NullPointerException.class, () -> $.takeWhile($.arrayList(1, 2, 3, 4, 5), null));

        {
            List<Integer> list = $.takeWhile($.arrayList(1, 2, 3, 4, 5), i -> i < 3);
            assertEquals("[1, 2]", list.toString());
        }
    }

    @Test
    void take_list() {
        {
            List<Integer> list = $.take($.arrayList(1, 2, 3, 4), 2);
            assertEquals("[1, 2]", list.toString());
        }
        {
            List<Integer> list = $.take($.arrayList(1, 2, 3, 4, 5), 6);
            assertEquals("[1, 2, 3, 4, 5]", list.toString());
        }
        {
            List<Integer> list = $.take($.arrayList(1, 2, 3, 4, 5), 3);
            assertEquals("[1, 2, 3]", list.toString());
        }
    }


    @Test
    void size() {
        assertEquals(0, $.size((List<Object>) null));
        assertEquals(0, $.size($.arrayList()));
        assertEquals(1, $.size($.arrayList(1)));

        assertEquals(0, $.size((Map<Object, Object>) null));
        assertEquals(0, $.size($.hashMap()));
        assertEquals(1, $.size($.hashMap(1, 1)));
    }

    @Test
    void orderBy() {
        {
            List<Integer> list = $.orderBy($.arrayList(1, 3, 5, 2, 4), Function.identity(), ASC);
            assertEquals("[1, 2, 3, 4, 5]", list.toString());
        }
        {
            List<Integer> list = $.orderBy($.arrayList(1, 3, 5, 2, 4), Function.identity(), DESC);
            assertEquals("[5, 4, 3, 2, 1]", list.toString());
        }
        {
            List<Integer> list = $.orderBy((List<Integer>) null, Function.identity(), DESC);
            assertEquals("[]", list.toString());
        }
        {
            List<String> list = $.orderBy($.arrayList("C1", "A2", "B3"), it -> Integer.parseInt(it.substring(1)), ASC);
            assertEquals("[C1, A2, B3]", list.toString());
        }
        {
            List<String> list = $.orderBy($.arrayList("C1", "A2", "B3"), Function.identity(), ASC);
            assertEquals("[A2, B3, C1]", list.toString());
        }
    }

    @Test
    void reverse() {
        {
            List<Integer> list = $.reverse($.arrayList(1, 2, 3));
            assertEquals("[3, 2, 1]", list.toString());
        }
        {
            List<Integer> list = $.reverse($.arrayList(1, 2, 3, 4));
            assertEquals("[4, 3, 2, 1]", list.toString());
        }
    }

    @Test
    void reduceIterable() {
        assertThrows(NullPointerException.class, () -> $.reduce($.arrayList(1), 1, null));
        assertEquals(20, $.reduce($.arrayList(1, 2, 3, 4), 10, Integer::sum));
    }

    @Test
    void reduceMap() {
        {
            TreeMap<Integer, Integer> treeMap = new TreeMap<>();
            treeMap.put(1, 1);
            treeMap.put(2, 2);
            treeMap.put(3, 3);
            Integer result = $.reduce(treeMap, 100, (sum, k, v) -> sum + k * 10 + v);
            assertEquals(166, result);
        }

        assertEquals(100, $.reduce((Map<Integer, Integer>) null, 100, (sum, k, v) -> sum + k * 10 + v));

        assertThrows(NullPointerException.class, () -> $.reduce($.hashMap(1, 1), 100, null));
    }

    @Test
    void map1() {
        assertThrows(NullPointerException.class, () -> $.map($.arrayList(1, 2, 3), (Function<Integer, Object>) null));

        {
            List<Integer> list = $.map($.arrayList(1, 2, 3), i -> i * 2);
            assertEquals("[2, 4, 6]", list.toString());
        }
        {
            List<Integer> list = $.map($.arrayList(1, 2, 3), i -> i % 2);
            assertEquals("[1, 0, 1]", list.toString());
        }
    }

    @Test
    void map2() {
        assertThrows(NullPointerException.class, () -> $.map($.arrayList(1, 2, 3), (ObjIntFunction<Integer, Object>) null));

        {
            List<Integer> list = $.map($.arrayList(1, 2, 3), (it, i) -> it + 10 * (i + 1));
            assertEquals("[11, 22, 33]", list.toString());
        }
    }

    @Test
    void listFromIterator() {
        {
            List<Integer> list = $.listFrom(null);
            assertEquals(0, list.size());
        }
        {
            List<Integer> list = $.listFrom($.arrayList(1, 2, 3).iterator());
            assertEquals("[1, 2, 3]", list.toString());
        }
    }

    @Test
    void keyBy() {
        {
            Map<Integer, Integer> map = $.keyBy($.arrayList(1, 4, 7, 2, 5, 3), i -> i % 3);
            assertEquals(3, map.size());
            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(0));
        }
        {
            Map<String, Integer> map = $.keyBy($.arrayList(1, 2, 3, 4, 5), i -> i % 2 == 0 ? "even" : "odd");
            assertEquals(2, map.size());
            assertEquals(1, map.get("odd"));
            assertEquals(2, map.get("even"));
        }
    }

    @SuppressWarnings("ConstantValue")
    @Test
    void isEmpty() {
        assertTrue($.isEmpty((Map<Object, Object>) null));
        assertTrue($.isEmpty($.hashMap()));

        assertFalse($.isNotEmpty((Map<Object, Object>) null));
        assertFalse($.isNotEmpty($.hashMap()));

        assertTrue($.isEmpty((List<Object>) null));
        assertTrue($.isEmpty($.arrayList()));

        assertFalse($.isNotEmpty((List<Object>) null));
        assertFalse($.isNotEmpty($.arrayList()));
    }

    @Test
    void groupBy() {
        {
            Map<Integer, List<Integer>> map = $.groupBy($.arrayList(1, 4, 7, 2, 5, 3), i -> i % 3);
            assertEquals(3, map.size());
            assertEquals("[1, 4, 7]", map.get(1).toString());
            assertEquals("[2, 5]", map.get(2).toString());
            assertEquals("[3]", map.get(0).toString());
        }

        {
            Map<String, List<Integer>> map = $.groupBy($.arrayList(1, 2, 3, 4, 5), i -> i % 2 == 0 ? "even" : "odd");
            assertEquals(2, map.size());
            assertEquals("[1, 3, 5]", map.get("odd").toString());
            assertEquals("[2, 4]", map.get("even").toString());
        }
    }

    @Test
    void forEach1() {
        List<Integer> t = new ArrayList<>();
        $.forEach($.arrayList(1, 2, 3), i -> t.add(i));
        assertEquals("[1, 2, 3]", t.toString());
    }

    @Test
    void forEach2() {
        List<Integer> t = new ArrayList<>();
        $.forEach($.arrayList(1, 2, 3), (it, index) -> t.add(it + (index + 1) * 10));
        assertEquals("[11, 22, 33]", t.toString());
    }

    @Test
    void flatMap() {
        {
            List<Integer> list = $.flatMap($.arrayList(1, 2), i -> $.arrayList(i * 10 + 1, i * 10 + 2));
            assertEquals("[11, 12, 21, 22]", list.toString());
        }
        {
            List<Integer> list = $.flatMap($.arrayList(1, 2, 3), i -> $.arrayList(i * 2, i * 3));
            assertEquals("[2, 3, 4, 6, 6, 9]", list.toString());
        }

    }








    @Test
    void mapKeys1() {
        assertThrows(NullPointerException.class, () -> $.mapKeys($.hashMap(1, 1), (Function<Integer, Object>) null));

        {
            TreeMap<Integer, Integer> treeMap = new TreeMap<>();
            treeMap.put(1, 1);
            treeMap.put(2, 2);
            treeMap.put(3, 3);
            treeMap.put(4, 4);
            treeMap.put(5, 5);
            treeMap.put(6, 6);

            Map<Integer, Integer> map = $.mapKeys(treeMap, i -> i % 3);
            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(0));
        }
    }

    @Test
    void mapKeys2() {
        assertThrows(NullPointerException.class, () -> $.mapKeys($.hashMap(1, 1), (BiFunction<Integer, Integer, Object>) null));

        {
            TreeMap<Integer, Integer> treeMap = new TreeMap<>();
            treeMap.put(1, 1);
            treeMap.put(2, 2);
            treeMap.put(3, 3);
            treeMap.put(4, 4);
            treeMap.put(5, 5);
            treeMap.put(6, 6);

            Map<Integer, Integer> map = $.mapKeys(treeMap, (key, value) -> (key + value) % 5);
            assertEquals(5, map.size());
            assertEquals(1, map.get(2));
            assertEquals(2, map.get(4));
            assertEquals(3, map.get(1));
            assertEquals(4, map.get(3));
            assertEquals(5, map.get(0));
        }
    }

    @Test
    void mapValues1() {
        assertThrows(NullPointerException.class, () -> $.mapValues($.hashMap(1, 1), (Function<Integer, Object>) null));

        {
            Map<Integer, Integer> map = $.mapValues($.hashMap(0, "", 1, "1", 2, "11", 3, "111"), String::length);
            assertEquals(4, map.size());
            assertEquals(0, map.get(0));
            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
        }
    }

    @Test
    void mapValues2() {
        assertThrows(NullPointerException.class, () -> $.mapValues($.hashMap(1, 1), (BiFunction<Integer, Integer, Object>) null));

        {
            Map<Integer, String> map = $.mapValues(
                    $.hashMap(0, "", 1, "1", 2, "11", 3, "111"),
                    (value, key) -> String.format("%d: %s", key, value));
            assertEquals(4, map.size());
            assertEquals("0: ", map.get(0));
            assertEquals("1: 1", map.get(1));
            assertEquals("2: 11", map.get(2));
            assertEquals("3: 111", map.get(3));
        }
    }

    @Test
    void lastAndTail() {
        assertTrue($.last(null).isEmpty());
        assertTrue($.tail(null).isEmpty());

        assertEquals($.tail($.arrayList(1, 2, 3)).orElse(-1), 3);
        assertEquals($.last($.arrayList(1, 2, 3)).orElse(-1), 3);

        assertTrue($.last($.arrayList(1, 2, null)).isEmpty());
        assertTrue($.tail($.arrayList(1, 2, null)).isEmpty());
    }
}
