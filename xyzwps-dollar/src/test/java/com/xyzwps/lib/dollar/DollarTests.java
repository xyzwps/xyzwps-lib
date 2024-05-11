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
    void size() {
        assertEquals(0, $.size((Map<Object, Object>) null));
        assertEquals(0, $.size($.hashMap()));
        assertEquals(1, $.size($.hashMap(1, 1)));
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


}
