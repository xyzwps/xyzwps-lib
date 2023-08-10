package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.util.ObjIntFunction;
import com.xyzwps.lib.dollar.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;
import static com.xyzwps.lib.dollar.Direction.*;

class DollarTests {

    @Test
    void zip() {
        {
            List<Pair<Integer, Integer>> list = $.zip($.listOf(1, 2, 3), $.listOf(1, 2));
            assertEquals("[(1, 1), (2, 2), (3, null)]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred TODO: 要不要直接返回 ArrayList 得了？
        }
        {
            List<Pair<Integer, Integer>> list = $.zip($.listOf(1, 2, 3), $.listOf(1, 2, 3));
            assertEquals("[(1, 1), (2, 2), (3, 3)]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Pair<Integer, Integer>> list = $.zip($.listOf(1, 2, 3), $.listOf(1, 2, 3, 4, 5));
            assertEquals("[(1, 1), (2, 2), (3, 3), (null, 4), (null, 5)]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Pair<Integer, Integer>> list = $.zip($.listOf(1, 2, 3), $.listOf());
            assertEquals("[(1, null), (2, null), (3, null)]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Pair<Integer, Integer>> list = $.zip($.listOf(1, 2, 3), null);
            assertEquals("[(1, null), (2, null), (3, null)]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.zip($.listOf(1, 2, 3), $.listOf(1, 2), (l, r) -> (l == null ? 0 : l) + (r == null ? 0 : r));
            assertEquals("[2, 4, 3]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }

        assertThrows(NullPointerException.class, () -> $.zip($.listOf(1), $.listOf(2), null));

        {
            List<Pair<Object, Object>> list = $.zip(null, null);
            assertEquals(list.size(), 0);
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void unique() {
        {
            List<Integer> list = $.unique($.listOf(1, 2, 1, 3));
            assertEquals("[1, 2, 3]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.unique($.listOf(1, 2, 1));
            assertEquals("[1, 2]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void uniqueBy() {
        assertThrows(NullPointerException.class, () -> $.uniqueBy($.listOf(1, 2, 1, 3, 4), (Function<Integer, Object>) null));

        {
            List<Integer> list = $.uniqueBy($.listOf(1, 2, 1, 3, 4), i -> i % 3);
            assertEquals("[1, 2, 3]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Double> list = $.uniqueBy($.listOf(1.2, 2.3, 1.4), Double::intValue);
            assertEquals("[1.2, 2.3]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void uniqueBy_withIndex() {
        assertThrows(NullPointerException.class, () -> $.uniqueBy($.listOf(1, 2, 1, 3, 4), (ObjIntFunction<Integer, Object>) null));

        {
            List<Integer> list = $.uniqueBy($.listOf(1, 2, 1, 3, 4), (it, i) -> (it + i) % 3);
            assertEquals("[1, 2, 4]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void toSet() {
        {
            Set<Integer> set = $.toSet(null);
            assertTrue(set instanceof HashSet); // Hashset preferred
            assertEquals(0, set.size());
        }
        {
            Set<Integer> set = $.toSet($.listOf(1, 2, 1, 3, 4));
            assertTrue(set instanceof HashSet); // Hashset preferred
            assertEquals(4, set.size());
            assertTrue(set.contains(1));
            assertTrue(set.contains(2));
            assertTrue(set.contains(3));
            assertTrue(set.contains(4));
        }
    }

    @Test
    void takeWhile() {
        assertThrows(NullPointerException.class, () -> $.takeWhile($.listOf(1, 2, 3, 4, 5), null));

        {
            List<Integer> list = $.takeWhile($.listOf(1, 2, 3, 4, 5), i -> i < 3);
            assertEquals("[1, 2]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void take() {
        {
            List<Integer> list = $.take($.listOf(1, 2, 3, 4), 2);
            assertEquals("[1, 2]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.take($.listOf(1, 2, 3, 4, 5), 6);
            assertEquals("[1, 2, 3, 4, 5]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.take($.listOf(1, 2, 3, 4, 5), 3);
            assertEquals("[1, 2, 3]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void size() {
        assertEquals(0, $.size((List<Object>) null));
        assertEquals(0, $.size($.listOf()));
        assertEquals(1, $.size($.listOf(1)));

        assertEquals(0, $.size((Map<Object, Object>) null));
        assertEquals(0, $.size($.mapOf()));
        assertEquals(1, $.size($.mapOf(1, 1)));
    }

    @Test
    void orderBy() {
        {
            List<Integer> list = $.orderBy($.listOf(1, 3, 5, 2, 4), Function.identity(), ASC);
            assertEquals("[1, 2, 3, 4, 5]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.orderBy($.listOf(1, 3, 5, 2, 4), Function.identity(), DESC);
            assertEquals("[5, 4, 3, 2, 1]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.orderBy((List<Integer>) null, Function.identity(), DESC);
            assertEquals("[]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<String> list = $.orderBy($.listOf("C1", "A2", "B3"), it -> Integer.parseInt(it.substring(1)), ASC);
            assertEquals("[C1, A2, B3]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<String> list = $.orderBy($.listOf("C1", "A2", "B3"), Function.identity(), ASC);
            assertEquals("[A2, B3, C1]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void reverse() {
        {
            List<Integer> list = $.reverse($.listOf(1, 2, 3));
            assertEquals("[3, 2, 1]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.reverse($.listOf(1, 2, 3, 4));
            assertEquals("[4, 3, 2, 1]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void reduceIterable() {
        assertThrows(NullPointerException.class, () -> $.reduce($.listOf(1), 1, null));
        assertEquals(20, $.reduce($.listOf(1, 2, 3, 4), 10, Integer::sum));
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

        assertThrows(NullPointerException.class, () -> $.reduce($.mapOf(1, 1), 100, null));
    }

    @Test
    void map1() {
        assertThrows(NullPointerException.class, () -> $.map($.listOf(1, 2, 3), (Function<Integer, Object>) null));

        {
            List<Integer> list = $.map($.listOf(1, 2, 3), i -> i * 2);
            assertEquals("[2, 4, 6]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.map($.listOf(1, 2, 3), i -> i % 2);
            assertEquals("[1, 0, 1]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void map2() {
        assertThrows(NullPointerException.class, () -> $.map($.listOf(1, 2, 3), (ObjIntFunction<Integer, Object>) null));

        {
            List<Integer> list = $.map($.listOf(1, 2, 3), (it, i) -> it + 10 * (i + 1));
            assertEquals("[11, 22, 33]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void listFromIterator() {
        {
            List<Integer> list = $.listFrom(null);
            assertEquals(0, list.size());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.listFrom($.listOf(1, 2, 3).iterator());
            assertEquals("[1, 2, 3]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void keyBy() {
        {
            Map<Integer, Integer> map = $.keyBy($.listOf(1, 4, 7, 2, 5, 3), i -> i % 3);
            assertEquals(3, map.size());
            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(0));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
        {
            Map<String, Integer> map = $.keyBy($.listOf(1, 2, 3, 4, 5), i -> i % 2 == 0 ? "even" : "odd");
            assertEquals(2, map.size());
            assertEquals(1, map.get("odd"));
            assertEquals(2, map.get("even"));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
    }

    @SuppressWarnings("ConstantValue")
    @Test
    void isEmpty() {
        assertTrue($.isEmpty((Map<Object, Object>) null));
        assertTrue($.isEmpty($.mapOf()));

        assertFalse($.isNotEmpty((Map<Object, Object>) null));
        assertFalse($.isNotEmpty($.mapOf()));

        assertTrue($.isEmpty((List<Object>) null));
        assertTrue($.isEmpty($.listOf()));

        assertFalse($.isNotEmpty((List<Object>) null));
        assertFalse($.isNotEmpty($.listOf()));

        assertTrue($.isEmpty((String) null));
        assertTrue($.isEmpty(""));

        assertFalse($.isNotEmpty((String) null));
        assertFalse($.isNotEmpty(""));
    }

    @Test
    void groupBy() {
        {
            Map<Integer, List<Integer>> map = $.groupBy($.listOf(1, 4, 7, 2, 5, 3), i -> i % 3);
            assertEquals(3, map.size());
            assertEquals("[1, 4, 7]", map.get(1).toString());
            assertEquals("[2, 5]", map.get(2).toString());
            assertEquals("[3]", map.get(0).toString());
            assertTrue(map instanceof HashMap); // HashMap preferred
        }

        {
            Map<String, List<Integer>> map = $.groupBy($.listOf(1, 2, 3, 4, 5), i -> i % 2 == 0 ? "even" : "odd");
            assertEquals(2, map.size());
            assertEquals("[1, 3, 5]", map.get("odd").toString());
            assertEquals("[2, 4]", map.get("even").toString());
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
    }

    @Test
    void forEach1() {
        List<Integer> t = new ArrayList<>();
        $.forEach($.listOf(1, 2, 3), i -> t.add(i));
        assertEquals("[1, 2, 3]", t.toString());
    }

    @Test
    void forEach2() {
        List<Integer> t = new ArrayList<>();
        $.forEach($.listOf(1, 2, 3), (it, index) -> t.add(it + (index + 1) * 10));
        assertEquals("[11, 22, 33]", t.toString());
    }

    @Test
    void flatMap() {
        {
            List<Integer> list = $.flatMap($.listOf(1, 2), i -> $.listOf(i * 10 + 1, i * 10 + 2));
            assertEquals("[11, 12, 21, 22]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.flatMap($.listOf(1, 2, 3), i -> $.listOf(i * 2, i * 3));
            assertEquals("[2, 3, 4, 6, 6, 9]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }

    }

    @Test
    void first() {
        assertEquals(Optional.of(1), $.first($.listOf(1, 2)));
        assertEquals(Optional.empty(), $.first($.listOf(null, 2)));
        assertEquals(Optional.empty(), $.head($.listOf()));
    }

    @Test
    void filter1() {
        {
            List<String> list = $.filter($.listOf("a", " ", null), Objects::nonNull);
            assertEquals("[a,  ]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.filter($.listOf(1, 2, 3, 4, 5), i -> i % 2 == 0);
            assertEquals("[2, 4]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.filter($.listOf(1, 2, 3, 4, 5), i -> i % 2 == 1);
            assertEquals("[1, 3, 5]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.filter(null, i -> i % 2 == 0);
            assertEquals("[]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        assertThrows(NullPointerException.class, () -> $.filter($.listOf(1, 2, 3, 4, 5), (Predicate<Integer>) null).toString());
    }

    @Test
    void filter2() {
        {
            List<Integer> list = $.filter($.listOf(1, 2, 3, 4, 5), (it, i) -> i % 2 == 0);
            assertEquals("[1, 3, 5]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.filter(null, (e, i) -> i % 2 == 0);
            assertEquals("[]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        assertThrows(NullPointerException.class, () -> $.filter($.listOf(1, 2, 3, 4, 5), (BiPredicate<Integer, Integer>) null).toString());
    }

    @Test
    void concat() {
        {
            List<Object> list = $.concat(
                    $.listOf("a", "", null),
                    null,
                    $.listOf("1", "2"),
                    $.listOf(),
                    $.listOf(null, "b"));
            assertEquals("[a, , null, 1, 2, null, b]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Object> list = $.concat();
            assertEquals("[]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Object> list = $.concat(null, null);
            assertEquals("[]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.concat($.listOf(1, 2), $.listOf(3, 4));
            assertEquals("[1, 2, 3, 4]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.concat($.listOf(1, 2), null, $.listOf(3, 4));
            assertEquals("[1, 2, 3, 4]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void compact() {
        {
            List<Object> list = $.compact($.listOf("a", "", null));
            assertEquals("[a]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Object> list = $.compact($.listOf(null, "", false, 0));
            assertEquals("[]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Object> list = $.compact($.listOf(null, 6, "", "哈哈", false, 0));
            assertEquals("[6, 哈哈]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Object> list = $.compact(null);
            assertEquals("[]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Object> list = $.compact($.listOf(null, 1, 0, true, false, "a", ""));
            assertEquals("[1, true, a]", $.compact(list).toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void chunk() {
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
        List<Integer> list = $.listOf(1, 2, 3, 4, 5, 6);
        for (int i = 1; i < cases.length; i++) {
            assertEquals(cases[i], $.chunk(list, i).toString());
        }

        assertThrows(IllegalArgumentException.class, () -> $.chunk(list, 0));

        {
            List<List<Integer>> chunks = $.chunk($.listOf(1, 2, 3, 4, 5), 2);
            assertEquals("[[1, 2], [3, 4], [5]]", chunks.toString());
            assertTrue(chunks instanceof ArrayList); // ArrayList preferred
            assertTrue(chunks.get(0) instanceof ArrayList); // ArrayList preferred
            assertTrue(chunks.get(1) instanceof ArrayList); // ArrayList preferred
            assertTrue(chunks.get(2) instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void defaultTo() {
        assertEquals(1, $.defaultTo(null, 1));
        assertEquals(2, $.defaultTo(2, 1));
        //noinspection ConstantValue
        assertNull($.defaultTo(null, null));
    }

    @Test
    void listOf() {
        {
            List<Integer> list = $.listOf();
            assertEquals("[]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.listOf(1);
            assertEquals("[1]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.listOf(1, 2);
            assertEquals("[1, 2]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.listOf(1, 2, 3);
            assertEquals("[1, 2, 3]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.listOf(1, 2, 3, 4);
            assertEquals("[1, 2, 3, 4]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
        {
            List<Integer> list = $.listOf(1, 2, 3, 4, 5);
            assertEquals("[1, 2, 3, 4, 5]", list.toString());
            assertTrue(list instanceof ArrayList); // ArrayList preferred
        }
    }

    @Test
    void mapOf() {
        {
            Map<Integer, Integer> map = $.mapOf();
            assertEquals(0, map.size());
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
        {
            Map<Integer, Integer> map = $.mapOf(1, 1);
            assertEquals(1, map.size());

            assertEquals(1, map.get(1));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
        {
            Map<Integer, Integer> map = $.mapOf(1, 1, 2, 2);
            assertEquals(2, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
        {
            Map<Integer, Integer> map = $.mapOf(1, 1, 2, 2, 3, 3);
            assertEquals(3, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
        {
            Map<Integer, Integer> map = $.mapOf(1, 1, 2, 2, 3, 3, 4, 4);
            assertEquals(4, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
        {
            Map<Integer, Integer> map = $.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5);
            assertEquals(5, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
        {
            Map<Integer, Integer> map = $.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6);
            assertEquals(6, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
        {
            Map<Integer, Integer> map = $.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7);
            assertEquals(7, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertEquals(7, map.get(7));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
        {
            Map<Integer, Integer> map = $.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8);
            assertEquals(8, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertEquals(7, map.get(7));
            assertEquals(8, map.get(8));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
        {
            Map<Integer, Integer> map = $.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9);
            assertEquals(9, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertEquals(7, map.get(7));
            assertEquals(8, map.get(8));
            assertEquals(9, map.get(9));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
        {
            Map<Integer, Integer> map = $.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10);
            assertEquals(10, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertEquals(7, map.get(7));
            assertEquals(8, map.get(8));
            assertEquals(9, map.get(9));
            assertEquals(10, map.get(10));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
    }

    @Test
    void mapKeys1() {
        assertThrows(NullPointerException.class, () -> $.mapKeys($.mapOf(1, 1), (Function<Integer, Object>) null));

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
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
    }

    @Test
    void mapKeys2() {
        assertThrows(NullPointerException.class, () -> $.mapKeys($.mapOf(1, 1), (BiFunction<Integer, Integer, Object>) null));

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
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
    }

    @Test
    void mapValues1() {
        assertThrows(NullPointerException.class, () -> $.mapValues($.mapOf(1, 1), (Function<Integer, Object>) null));

        {
            Map<Integer, Integer> map = $.mapValues($.mapOf(0, "", 1, "1", 2, "11", 3, "111"), String::length);
            assertEquals(4, map.size());
            assertEquals(0, map.get(0));
            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
    }

    @Test
    void mapValues2() {
        assertThrows(NullPointerException.class, () -> $.mapValues($.mapOf(1, 1), (BiFunction<Integer, Integer, Object>) null));

        {
            Map<Integer, String> map = $.mapValues(
                    $.mapOf(0, "", 1, "1", 2, "11", 3, "111"),
                    (value, key) -> String.format("%d: %s", key, value));
            assertEquals(4, map.size());
            assertEquals("0: ", map.get(0));
            assertEquals("1: 1", map.get(1));
            assertEquals("2: 11", map.get(2));
            assertEquals("3: 111", map.get(3));
            assertTrue(map instanceof HashMap); // HashMap preferred
        }
    }

    @Test
    void pad() {
        assertThrows(IllegalArgumentException.class, () -> $.pad("", -1, " "));

        assertEquals("      ", $.pad(null, 6, null));
        assertEquals("      ", $.pad(null, 6, ""));
        assertEquals("      ", $.pad(null, 6, " "));
        assertEquals("aaaaaa", $.pad(null, 6, "a"));
        assertEquals("ababab", $.pad(null, 6, "ab"));
        assertEquals("abcdab", $.pad(null, 6, "abcd"));

        assertEquals("      ", $.pad("", 6, null));
        assertEquals("      ", $.pad("", 6, ""));
        assertEquals("      ", $.pad("", 6, " "));
        assertEquals("aaaaaa", $.pad("", 6, "a"));
        assertEquals("ababab", $.pad("", 6, "ab"));
        assertEquals("abcdab", $.pad("", 6, "abcd"));

        assertEquals(" +++  ", $.pad("+++", 6, null));
        assertEquals(" +++  ", $.pad("+++", 6, ""));
        assertEquals(" +++  ", $.pad("+++", 6, " "));
        assertEquals("a+++aa", $.pad("+++", 6, "a"));
        assertEquals("a+++ba", $.pad("+++", 6, "ab"));
        assertEquals("a+++bc", $.pad("+++", 6, "abcd"));

        assertEquals("+++---***", $.pad("+++---***", 6, "abcd"));
    }

    @Test
    void padEnd() {
        assertThrows(IllegalArgumentException.class, () -> $.padEnd("", -1, " "));

        assertEquals("      ", $.padEnd(null, 6, null));
        assertEquals("      ", $.padEnd(null, 6, ""));
        assertEquals("      ", $.padEnd(null, 6, " "));
        assertEquals("aaaaaa", $.padEnd(null, 6, "a"));
        assertEquals("ababab", $.padEnd(null, 6, "ab"));
        assertEquals("abcdab", $.padEnd(null, 6, "abcd"));

        assertEquals("      ", $.padEnd("", 6, null));
        assertEquals("      ", $.padEnd("", 6, ""));
        assertEquals("      ", $.padEnd("", 6, " "));
        assertEquals("aaaaaa", $.padEnd("", 6, "a"));
        assertEquals("ababab", $.padEnd("", 6, "ab"));
        assertEquals("abcdab", $.padEnd("", 6, "abcd"));

        assertEquals("+++   ", $.padEnd("+++", 6, null));
        assertEquals("+++   ", $.padEnd("+++", 6, ""));
        assertEquals("+++   ", $.padEnd("+++", 6, " "));
        assertEquals("+++aaa", $.padEnd("+++", 6, "a"));
        assertEquals("+++aba", $.padEnd("+++", 6, "ab"));
        assertEquals("+++abc", $.padEnd("+++", 6, "abcd"));

        assertEquals("+++---***", $.padEnd("+++---***", 6, "abcd"));
    }

    @Test
    void padStart() {
        assertThrows(IllegalArgumentException.class, () -> $.padStart("", -1, " "));

        assertEquals("      ", $.padStart(null, 6, null));
        assertEquals("      ", $.padStart(null, 6, ""));
        assertEquals("      ", $.padStart(null, 6, " "));
        assertEquals("aaaaaa", $.padStart(null, 6, "a"));
        assertEquals("ababab", $.padStart(null, 6, "ab"));
        assertEquals("abcdab", $.padStart(null, 6, "abcd"));

        assertEquals("      ", $.padStart("", 6, null));
        assertEquals("      ", $.padStart("", 6, ""));
        assertEquals("      ", $.padStart("", 6, " "));
        assertEquals("aaaaaa", $.padStart("", 6, "a"));
        assertEquals("ababab", $.padStart("", 6, "ab"));
        assertEquals("abcdab", $.padStart("", 6, "abcd"));

        assertEquals("   +++", $.padStart("+++", 6, null));
        assertEquals("   +++", $.padStart("+++", 6, ""));
        assertEquals("   +++", $.padStart("+++", 6, " "));
        assertEquals("aaa+++", $.padStart("+++", 6, "a"));
        assertEquals("aba+++", $.padStart("+++", 6, "ab"));
        assertEquals("abc+++", $.padStart("+++", 6, "abcd"));

        assertEquals("+++---***", $.padStart("+++---***", 6, "abcd"));
    }

    @Test
    void lastAndTail() {
        assertTrue($.last(null).isEmpty());
        assertTrue($.tail(null).isEmpty());

        assertEquals($.tail($.listOf(1, 2, 3)).orElse(-1), 3);
        assertEquals($.last($.listOf(1, 2, 3)).orElse(-1), 3);

        assertTrue($.last($.listOf(1, 2, null)).isEmpty());
        assertTrue($.tail($.listOf(1, 2, null)).isEmpty());
    }
}
