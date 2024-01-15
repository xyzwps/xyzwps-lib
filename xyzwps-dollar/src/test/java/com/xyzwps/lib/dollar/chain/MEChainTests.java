package com.xyzwps.lib.dollar.chain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.xyzwps.lib.dollar.Dollar.$.*;
import static org.junit.jupiter.api.Assertions.*;


class MEChainTests {

    /*
    @Test
    void nullMap() {
        Map<Integer, Integer> map = MEChain.from((Map<Integer, Integer>) null).value();
        assertEquals(0, map.size());
    }

    @Test
    void mapValues1() {
        Map<Integer, Integer> map = MEChain.from(mapOf(0, "", 1, "1", 2, "11", 3, "111"))
                .mapValues(String::length)
                .value();
        assertEquals(4, map.size());
        assertEquals(0, map.get(0));
        assertEquals(1, map.get(1));
        assertEquals(2, map.get(2));
        assertEquals(3, map.get(3));

        assertThrows(NullPointerException.class, () -> MEChain.from(mapOf()).mapValues((Function<Object, Object>) null))
        ;
    }

    @Test
    void mapValues2() {
        Map<Integer, String> map = MEChain.from(mapOf(0, "", 1, "1", 2, "11", 3, "111"))
                .mapValues((value, key) -> String.format("%d: %s", key, value))
                .value();
        assertEquals(4, map.size());
        assertEquals("0: ", map.get(0));
        assertEquals("1: 1", map.get(1));
        assertEquals("2: 11", map.get(2));
        assertEquals("3: 111", map.get(3));

        assertThrows(NullPointerException.class, () -> MEChain.from(mapOf()).mapValues((BiFunction<Object, Object, Object>) null));
    }

    @Test
    void mapKeys1() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(3, 3);
        treeMap.put(4, 4);
        treeMap.put(5, 5);
        treeMap.put(6, 6);
        Map<Integer, Integer> map = MEChain.from(treeMap)
                .mapKeys(i -> i % 3)
                .value();
        assertEquals(1, map.get(1));
        assertEquals(2, map.get(2));
        assertEquals(3, map.get(0));

        assertThrows(NullPointerException.class, () -> MEChain.from(mapOf()).mapKeys((Function<Object, Object>) null));
    }

    @Test
    void mapKeys2() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(3, 3);
        treeMap.put(4, 4);
        treeMap.put(5, 5);
        treeMap.put(6, 6);
        Map<Integer, Integer> map = MEChain.from(treeMap)
                .mapKeys((key, value) -> (key + value) % 5)
                .value();
        assertEquals(5, map.size());
        assertEquals(1, map.get(2));
        assertEquals(2, map.get(4));
        assertEquals(3, map.get(1));
        assertEquals(4, map.get(3));
        assertEquals(5, map.get(0));

        assertThrows(NullPointerException.class, () -> MEChain.from(mapOf()).mapKeys((BiFunction<Object, Object, Object>) null));
    }


    @Test
    void filter() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(3, 3);
        treeMap.put(4, 4);
        treeMap.put(5, 5);
        treeMap.put(6, 6);
        Map<Integer, Integer> map = MEChain.from(treeMap)
                .filter((key, value) -> value % 2 == 0)
                .value();
        assertEquals(2, map.get(2));
        assertEquals(4, map.get(4));
        assertEquals(6, map.get(6));
        assertEquals(3, map.size());

        assertThrows(NullPointerException.class, () -> MEChain.from(mapOf()).filter(null));
    }

    @Test
    void values() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(3, 3);
        treeMap.put(4, 4);
        treeMap.put(5, 5);
        treeMap.put(6, 6);
        List<Integer> list = MEChain.from(treeMap).values().value();
        assertIterableEquals(listOf(1, 2, 3, 4, 5, 6), list);
    }

    @Test
    void value() {
        Map<Integer, Integer> map = MEChain.from(mapOf(1, 1, 2, 2, 3, 3)).value();
        assertTrue(map instanceof HashMap); // HashMap preferred
    }

    @Test
    void reduce() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(3, 3);
        Integer result = MEChain.from(treeMap).reduce(100, (sum, k, v) -> sum + k * 10 + v);
        assertEquals(166, result);

        assertEquals(100, MEChain.from((Map<Integer, Integer>) null).reduce(100, (sum, k, v) -> sum + k * 10 + v));

        assertThrows(NullPointerException.class, () -> MEChain.from(treeMap).reduce(100, null));
    }

    @Test
    void keys() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(3, 3);
        treeMap.put(4, 4);
        treeMap.put(5, 5);
        treeMap.put(6, 6);
        List<Integer> list = MEChain.from(treeMap).keys().value();
        assertIterableEquals(listOf(1, 2, 3, 4, 5, 6), list);
    }

    @Test
    void forEach() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(3, 3);
        treeMap.put(4, 4);
        treeMap.put(5, 5);
        treeMap.put(6, 6);
        List<Boolean> list = listOf(false, false, false, false, false, false, false);
        MEChain.from(treeMap).forEach((key, value) -> {
            switch (key) {
                case 1, 2, 3, 4, 5, 6 -> {
                    list.set(key, true);
                    assertEquals(key, value);
                }
                default -> throw new IllegalStateException();
            }
        });
        assertIterableEquals(listOf(false, true, true, true, true, true, true), list);

        assertThrows(NullPointerException.class, () -> MEChain.from(treeMap).forEach((BiConsumer<Integer, Integer>) null));
    }
*/
}
