package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.util.*;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

public record ChainCases() {

    /**
     * Run all test cases.
     */
    public void test() {
        testChain();
        testMapEntryChain();
    }

    void testChain() {
        testChunk();
        testCompact();
        testConcat();
        testFilter();
        testFilter2();
        testFirst();
        testFlatMap();
        testForEach();
        testForEach2();
        testGroupBy();
        testHead();
        testIterator();
        testJoin();
        testKeyBy();
        testMap();
        testMap2();
        testOrderBy();
        testReduce();
        testReverse();
        testSize();
        testSkip();
        testSkipWhile();
        testTake();
        testTakeWhile();
        testToList();
        testToSet();
        testUnique();
        testUniqueBy();
        testZip();
    }

    void testMapEntryChain() {
        testMEForEach();
        testMEFilter();
        testMEKeys();
        testMEMapKeys();
        testMEMapKeys2();
        testMEMapValues();
        testMEMapValues2();
        testMEReduce();
        testMEValues();
    }

    void testMEValues() {
        assertTrue($(Map.of()).values().toList().isEmpty());

        // laziness
        {
            var map = treeMapOf(Pair.of(1, 100), Pair.of(2, 200), Pair.of(3, 300));
            var actions = new ArrayList<String>();
            var c = $(map)
                    .mapKeys(it -> {
                        actions.add("mapKey " + it);
                        return it;
                    })
                    .values();
            assertTrue(actions.isEmpty());
            assertEquals("[100, 200, 300]", c.toList().toString());
            assertIterableEquals(List.of("mapKey 1", "mapKey 2", "mapKey 3"), actions);
        }
    }

    void testMEReduce() {
        assertThrows(NullPointerException.class, () -> $(Map.of()).reduce(new Object(), null));

        var map = treeMapOf(Pair.of(1, 100), Pair.of(2, 200), Pair.of(3, 300));
        assertEquals(1400, $(map).reduce(0, (r, k, v) -> r + k * v));
    }

    void testMEMapValues2() {
        assertThrows(NullPointerException.class, () -> $(Map.of()).mapValues((BiFunction<Object, Object, Object>) null));

        // laziness
        {
            var map = treeMapOf(Pair.of(1, 100), Pair.of(2, 200), Pair.of(3, 300));
            var actions = new ArrayList<String>();
            var c = $(map)
                    .mapValues((v, k) -> {
                        actions.add("mapValue " + v);
                        return (v + 1) * k;
                    });
            assertTrue(actions.isEmpty());
            var result = c.toMap();
            assertIterableEquals(List.of("mapValue 100", "mapValue 200", "mapValue 300"), actions);
            assertEquals(3, result.size());
            assertEquals(101, result.get(1));
            assertEquals(402, result.get(2));
            assertEquals(903, result.get(3));
        }
    }

    void testMEMapValues() {
        assertThrows(NullPointerException.class, () -> $(Map.of()).mapValues((Function<Object, Object>) null));

        // laziness
        {
            var map = treeMapOf(Pair.of(1, 100), Pair.of(2, 200), Pair.of(3, 300));
            var actions = new ArrayList<String>();
            var c = $(map)
                    .mapValues(it -> {
                        actions.add("mapValue " + it);
                        return it + 1;
                    });
            assertTrue(actions.isEmpty());
            var result = c.toMap();
            assertIterableEquals(List.of("mapValue 100", "mapValue 200", "mapValue 300"), actions);
            assertEquals(3, result.size());
            assertEquals(101, result.get(1));
            assertEquals(201, result.get(2));
            assertEquals(301, result.get(3));
        }
    }

    void testMEMapKeys2() {
        assertThrows(NullPointerException.class, () -> $(Map.of()).mapKeys((BiFunction<Object, Object, Object>) null));

        // laziness
        {
            var map = treeMapOf(Pair.of(1, 100), Pair.of(2, 200), Pair.of(3, 300));
            var actions = new ArrayList<String>();
            var c = $(map)
                    .mapKeys((k, v) -> {
                        actions.add("mapKey " + k);
                        return k + v;
                    });
            assertTrue(actions.isEmpty());
            var result = c.toMap();
            assertIterableEquals(List.of("mapKey 1", "mapKey 2", "mapKey 3"), actions);
            assertEquals(3, result.size());
            assertEquals(100, result.get(101));
            assertEquals(200, result.get(202));
            assertEquals(300, result.get(303));
        }
    }

    void testMEMapKeys() {
        assertThrows(NullPointerException.class, () -> $(Map.of()).mapKeys((Function<Object, Object>) null));

        // laziness
        {
            var map = treeMapOf(Pair.of(1, 100), Pair.of(2, 200), Pair.of(3, 300));
            var actions = new ArrayList<String>();
            var c = $(map)
                    .mapKeys(it -> {
                        actions.add("mapKey " + it);
                        return it * 2;
                    });
            assertTrue(actions.isEmpty());
            var result = c.toMap();
            assertIterableEquals(List.of("mapKey 1", "mapKey 2", "mapKey 3"), actions);
            assertEquals(3, result.size());
            assertEquals(100, result.get(2));
            assertEquals(200, result.get(4));
            assertEquals(300, result.get(6));
        }
    }

    void testMEKeys() {
        assertTrue($(Map.of()).keys().toList().isEmpty());

        // laziness
        {
            var map = treeMapOf(Pair.of(1, 100), Pair.of(2, 200), Pair.of(3, 300));
            var actions = new ArrayList<String>();
            var c = $(map)
                    .mapKeys(it -> {
                        actions.add("mapKey " + it);
                        return it;
                    })
                    .keys();
            assertTrue(actions.isEmpty());
            assertEquals("[1, 2, 3]", c.toList().toString());
            assertIterableEquals(List.of("mapKey 1", "mapKey 2", "mapKey 3"), actions);
        }
    }

    void testMEFilter() {
        assertThrows(NullPointerException.class, () -> $(Map.of()).filter(null));

        var map = treeMapOf(Pair.of(1, 100), Pair.of(2, 200), Pair.of(3, 300));

        // filter keys && laziness
        {
            var actions = new ArrayList<String>();
            var c = $(map)
                    .filter((k, v) -> {
                        actions.add(String.format("filter %d %d", k, v));
                        return k % 2 == 1;
                    });
            assertTrue(actions.isEmpty());
            var result = c.toMap();
            assertIterableEquals(List.of("filter 1 100", "filter 2 200", "filter 3 300"), actions);
            assertEquals(2, result.size());
            assertEquals(100, result.get(1));
            assertEquals(300, result.get(3));
        }

        // filter values && laziness
        {
            var actions = new ArrayList<String>();
            var c = $(map)
                    .filter((k, v) -> {
                        actions.add(String.format("filter %d %d", k, v));
                        return v > 100;
                    });
            assertTrue(actions.isEmpty());
            var result = c.toMap();
            assertIterableEquals(List.of("filter 1 100", "filter 2 200", "filter 3 300"), actions);
            assertEquals(2, result.size());
            assertEquals(200, result.get(2));
            assertEquals(300, result.get(3));
        }
    }

    void testMEForEach() {
        assertThrows(NullPointerException.class, () -> $(Map.of()).forEach(null));

        var map = treeMapOf(Pair.of(1, "1"), Pair.of(2, "2"), Pair.of(3, "3"));
        var actions = new ArrayList<String>();
        $(map).forEach((k, v) -> actions.add(String.format("k %d, v %s", k, v)));
        assertEquals(3, actions.size());
        assertIterableEquals(List.of("k 1, v 1", "k 2, v 2", "k 3, v 3"), actions);
    }

    @SafeVarargs
    static <K extends Comparable<K>, V> TreeMap<K, V> treeMapOf(Pair<K, V>... pairs) {
        var m = new TreeMap<K, V>();
        for (var p : pairs) {
            m.put(p.key(), p.value());
        }
        return m;
    }


    void testKeyBy() {
        assertThrows(NullPointerException.class, () -> $.just(1, 2, 3).keyBy(null));

        // keep the first met
        {
            var map = $.just(1, 2, 3, 4, 5).keyBy(i -> i % 3).toMap();
            assertEquals(3, map.size());
            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(0));
        }

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.just(1, 2, 3)
                    .map(i -> {
                        actions.add("map " + i);
                        return i;
                    })
                    .keyBy(Function.identity())
                    .mapKeys((key) -> {
                        actions.add("map key");
                        return key;
                    });
            assertTrue(actions.isEmpty());

            var map = c.toMap();
            assertEquals(3, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));

            assertIterableEquals(List.of(
                    "map 1", "map key",
                    "map 2", "map key",
                    "map 3", "map key"
            ), actions);
        }
    }

    void testGroupBy() {
        assertThrows(NullPointerException.class, () -> $.just(1, 2, 3).groupBy(null));

        // laziness
        {
            var mapKeysActions = new HashMap<Integer, String>();
            var actions = new ArrayList<String>();
            var c = $.just(1, 2, 3, 4, 5, 6, 7)
                    .map(it -> {
                        actions.add("map " + it);
                        return it;
                    })
                    .groupBy(i -> i % 3)
                    .mapKeys((key, value) -> {
                        mapKeysActions.put(key, String.format("mapkey %s %s", key, value));
                        actions.add("mapkey");
                        return key;
                    });
            assertTrue(actions.isEmpty());
            assertTrue(mapKeysActions.isEmpty());

            var map = c.toMap();
            assertEquals("[3, 6]", map.get(0).toString());
            assertEquals("[1, 4, 7]", map.get(1).toString());
            assertEquals("[2, 5]", map.get(2).toString());
            assertEquals(3, map.size());

            assertIterableEquals(List.of(
                    "map 1", "map 2", "map 3", "map 4", "map 5", "map 6", "map 7",
                    "mapkey", "mapkey", "mapkey"
            ), actions);
            assertEquals(3, mapKeysActions.size());
            assertEquals("mapkey 0 [3, 6]", mapKeysActions.get(0));
            assertEquals("mapkey 1 [1, 4, 7]", mapKeysActions.get(1));
            assertEquals("mapkey 2 [2, 5]", mapKeysActions.get(2));
        }
    }

    void testUniqueBy() {
        assertEquals("[1, 2, 3, 4]", $.just(1, 2, 1, 2, 3, 1, 2, 3, 4)
                .uniqueBy(i -> i % 4)
                .toList().toString());

        assertThrows(NullPointerException.class, () -> $.just(1, 2).uniqueBy(null));

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.just(1, 2, 2, 3, 4, 1, 4)
                    .map(i -> {
                        actions.add("map1 " + i);
                        return i;
                    })
                    .uniqueBy(i -> i % 4)
                    .map(i -> {
                        actions.add("map2 " + i);
                        return i;
                    });
            assertTrue(actions.isEmpty());
            assertEquals("[1, 2, 3, 4]", c.toList().toString());
            assertIterableEquals(List.of(
                    "map1 1", "map2 1",
                    "map1 2", "map2 2",
                    "map1 2",
                    "map1 3", "map2 3",
                    "map1 4", "map2 4",
                    "map1 1",
                    "map1 4"
            ), actions);
        }
    }

    void testUnique() {
        assertEquals("[1, 2, 3, 4]", $.just(1, 2, 1, 2, 3, 1, 2, 3, 4).unique().toList().toString());

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.just(1, 2, 2, 3, 4, 1, 4)
                    .map(i -> {
                        actions.add("map1 " + i);
                        return i;
                    })
                    .unique()
                    .map(i -> {
                        actions.add("map2 " + i);
                        return i;
                    });
            assertTrue(actions.isEmpty());
            assertEquals("[1, 2, 3, 4]", c.toList().toString());
            assertIterableEquals(List.of(
                    "map1 1", "map2 1",
                    "map1 2", "map2 2",
                    "map1 2",
                    "map1 3", "map2 3",
                    "map1 4", "map2 4",
                    "map1 1",
                    "map1 4"
            ), actions);
        }
    }

    void testToSet() {
        assertEquals("[]", $.empty().toSet().toString());

        assertEquals("[]", $.just().toSet().toString());
        {
            var set = $.just(1, 2, 1).toSet();
            assertEquals(set.size(), 2);
            assertTrue(set.contains(1));
            assertTrue(set.contains(2));
        }

        assertEquals("[]", $((Iterable<?>) null).toSet().toString());
        {
            var set = $(List.of(1, 2, 1, 2)).toSet();
            assertEquals(set.size(), 2);
            assertTrue(set.contains(1));
            assertTrue(set.contains(2));
        }

        {
            var set = $.range(1, 3).toSet();
            assertEquals(set.size(), 2);
            assertTrue(set.contains(1));
            assertTrue(set.contains(2));
        }
        assertEquals("[]", $.range(1, 1).toSet().toString());
        {
            var set = $.range(1, -3).toSet();
            assertEquals(set.size(), 4);
            assertTrue(set.contains(1));
            assertTrue(set.contains(0));
            assertTrue(set.contains(-1));
            assertTrue(set.contains(-2));
        }
    }

    void testToList() {
        assertEquals("[]", $.empty().toList().toString());

        assertEquals("[]", $.just().toList().toString());
        assertEquals("[1, 2]", $.just(1, 2).toList().toString());

        assertEquals("[]", $((Iterable<?>) null).toList().toString());
        assertEquals("[1, 2]", $(List.of(1, 2)).toList().toString());

        assertEquals("[1, 2]", $.range(1, 3).toList().toString());
        assertEquals("[]", $.range(1, 1).toList().toString());
        assertEquals("[1, 0, -1, -2]", $.range(1, -3).toList().toString());
    }

    void testTakeWhile() {
        assertEquals("[1, 2, 3]", $.just(1, 2, 3, 4, 5).takeWhile(it -> it < 4).toList().toString());
        assertEquals("[]", $.just(1, 2, 3, 4, 5).takeWhile(it -> it < 0).toList().toString());

        assertThrows(NullPointerException.class, () -> $.just(1, 2, 3, 4, 5).takeWhile(null));

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.just(1, 2, 3, 4, 5)
                    .map(it -> {
                        actions.add("map1 " + it);
                        return it * 2;
                    })
                    .takeWhile(it -> it < 8)
                    .map(it -> {
                        actions.add("map2 " + it);
                        return it + 2;
                    });
            assertTrue(actions.isEmpty());
            assertEquals("[4, 6, 8]", c.toList().toString());
            assertIterableEquals(List.of(
                    "map1 1", "map2 2",
                    "map1 2", "map2 4",
                    "map1 3", "map2 6",
                    "map1 4"
            ), actions);
        }
    }

    void testSkipWhile() {
        assertEquals("[4, 5]", $.just(1, 2, 3, 4, 5).skipWhile(it -> it < 4).toList().toString());
        assertEquals("[]", $.just(1, 2, 3, 4, 5).skipWhile(it -> it < 6).toList().toString());

        assertThrows(NullPointerException.class, () -> $.just(1, 2, 3, 4, 5).skipWhile(null));

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.just(1, 2, 3, 4, 5)
                    .map(it -> {
                        actions.add("map1 " + it);
                        return it * 2;
                    })
                    .skipWhile(it -> it < 6)
                    .map(it -> {
                        actions.add("map2 " + it);
                        return it + 2;
                    });
            assertTrue(actions.isEmpty());
            assertEquals("[8, 10, 12]", c.toList().toString());
            assertIterableEquals(List.of(
                    "map1 1",
                    "map1 2",
                    "map1 3", "map2 6",
                    "map1 4", "map2 8",
                    "map1 5", "map2 10"
            ), actions);
        }
    }

    void testSkip() {
        assertEquals("[4, 5]", $.just(1, 2, 3, 4, 5).skip(3).toList().toString());
        assertEquals("[]", $.just(1, 2, 3, 4, 5).skip(5).toList().toString());
        assertEquals("[1, 2, 3, 4, 5]", $.just(1, 2, 3, 4, 5).skip(0).toList().toString());
        assertEquals("[1, 2, 3, 4, 5]", $.just(1, 2, 3, 4, 5).skip(-1).toList().toString());

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.just(1, 2, 3, 4, 5)
                    .map(it -> {
                        actions.add("map1 " + it);
                        return it * 2;
                    })
                    .skip(3)
                    .map(it -> {
                        actions.add("map2 " + it);
                        return it + 2;
                    });
            assertTrue(actions.isEmpty());
            assertEquals("[10, 12]", c.toList().toString());
            assertIterableEquals(List.of(
                    "map1 1",
                    "map1 2",
                    "map1 3",
                    "map1 4", "map2 8",
                    "map1 5", "map2 10"
            ), actions);
        }
    }

    void testSize() {
        assertEquals(0, $.just().size());
        assertEquals(3, $.just(1, 2, 3).size());
    }

    void testReverse() {
        assertEquals("[1, 2, 3, 4, 5]", $.just(5, 4, 3, 2, 1).reverse().toList().toString());

        // laziness
        var actions = new ArrayList<String>();
        var c = $.just(1, 2, 3, 4, 5)
                .map((it) -> {
                    actions.add("map1 " + it);
                    return it * 2;
                })
                .reverse()
                .map(it -> {
                    actions.add("map2 " + it);
                    return it / 2;
                });
        assertTrue(actions.isEmpty());
        assertEquals("[5, 4, 3, 2, 1]", c.toList().toString());
        assertIterableEquals(List.of(
                "map1 1", "map1 2", "map1 3", "map1 4", "map1 5",
                "map2 10", "map2 8", "map2 6", "map2 4", "map2 2"
        ), actions);
    }

    void testReduce() {
        assertEquals(15, $.just(1, 2, 3, 4, 5).reduce(0, Integer::sum));

        assertThrows(NullPointerException.class, () -> $.just(1, 2, 3, 4, 5).reduce(0, null));
    }

    void testOrderBy() {
        assertEquals("[1, 2, 3, 4, 5]", $.just(1, 4, 3, 2, 5).orderBy(Function.identity(), Direction.ASC).toList().toString());
        assertEquals("[5, 4, 3, 2, 1]", $.just(1, 4, 3, 2, 5).orderBy(Function.identity(), Direction.DESC).toList().toString());

        assertThrows(NullPointerException.class, () -> $.just(1, 4, 3, 2, 5).orderBy(null, Direction.ASC));
        assertThrows(NullPointerException.class, () -> $.just(1, 4, 3, 2, 5).orderBy(null, Direction.DESC));
        assertThrows(NullPointerException.class, () -> $.just(1, 4, 3, 2, 5).orderBy(Function.identity(), null));

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.just(1.1, 4.1, 3.1, 2.1, 5.1)
                    .map((it, i) -> {
                        actions.add("map " + i);
                        return it.intValue();
                    })
                    .orderBy(Function.identity(), Direction.ASC)
                    .map(it -> {
                        actions.add("map2 " + it);
                        return it * 2;
                    });
            assertTrue(actions.isEmpty());
            assertEquals("[2, 4, 6, 8, 10]", c.toList().toString());
            assertIterableEquals(List.of(
                    "map 0", "map 1", "map 2", "map 3", "map 4",
                    "map2 1", "map2 2", "map2 3", "map2 4", "map2 5"
            ), actions);
        }
    }

    void testMap2() {
        assertEquals("[1, 4, 9, 16]", $.just(1, 2, 3, 4).map((it, i) -> it * (i + 1)).toList().toString());

        assertThrows(NullPointerException.class, () -> $.just(1, 2, 3, 4).map((ObjIntFunction<Integer, Object>) null));

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.just(1, 2, 3, 4)
                    .map((it, i) -> {
                        actions.add(String.format("map(%d, %d)", it, i));
                        return it * (i + 1);
                    });
            assertTrue(actions.isEmpty());
            assertEquals("[1, 4, 9, 16]", c.toList().toString());
            assertIterableEquals(List.of("map(1, 0)", "map(2, 1)", "map(3, 2)", "map(4, 3)"), actions);
        }
    }

    void testMap() {
        assertEquals("[2, 4, 6, 8]", $.just(1, 2, 3, 4).map(i -> i * 2).toList().toString());

        assertThrows(NullPointerException.class, () -> $.just(1, 2, 3, 4).map((Function<Integer, Object>) null));

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.just(1, 2, 3, 4)
                    .map(i -> {
                        actions.add("map " + i);
                        return i * 2;
                    });
            assertTrue(actions.isEmpty());
            assertEquals("[2, 4, 6, 8]", c.toList().toString());
            assertIterableEquals(List.of("map 1", "map 2", "map 3", "map 4"), actions);
        }
    }

    void testJoin() {
        assertEquals("1234", $.just(1, 2, 3, 4).join(""));
        assertEquals("1-2-3-4", $.just(1, 2, 3, 4).join("-"));
        assertEquals("1null2null3null4", $.just(1, 2, 3, 4).join(null));
    }

    void testIterator() {
        {
            var itr = $.just(1, 2, 3, 4).iterator();
            var counter = new Counter(1);
            while (itr.hasNext()) {
                assertEquals(itr.next(), counter.getAndIncr());
            }
        }

        {
            // laziness
            var actions = new ArrayList<String>();
            var itr = $.just(1, 2, 4)
                    .flatMap(i -> {
                        actions.add("flatmap " + i);
                        return $.range(i, i * 2);
                    })
                    .iterator();
//            assertTrue(actions.isEmpty()); TODO: 搞一个 lazi iterator
            var counter = new Counter(1);
            while (itr.hasNext()) {
                assertEquals(itr.next(), counter.getAndIncr());
            }
            assertIterableEquals(List.of("flatmap 1", "flatmap 2", "flatmap 4"), actions);
        }
    }

    void testForEach2() {
        var list = new ArrayList<Integer>();
        $.just(1, 2, 3).forEach((it, i) -> {
            list.add(i);
            list.add(it);
        });
        assertEquals("[0, 1, 1, 2, 2, 3]", list.toString());

        assertThrows(NullPointerException.class, () -> $.just(1, 2, 3).forEach((ObjIntConsumer<Integer>) null));
    }

    void testForEach() {
        var list = new ArrayList<Integer>();
        $.just(1, 2, 3).forEach(i -> list.add(i));
        assertEquals("[1, 2, 3]", list.toString());

        assertThrows(NullPointerException.class, () -> $.just(1, 2, 3).forEach((Consumer<Integer>) null));
    }

    void testFlatMap() {
        assertEquals("[1, 1, 2, 1, 2, 3]", $.just(1, 2, 3).flatMap(i -> Seq.range(1, 1 + i)).toList().toString());

        assertThrows(NullPointerException.class, () -> $.just(1, 2, 3).flatMap(null));

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.just(1, 2, 3).flatMap(i -> {
                actions.add("flatmap " + i);
                return $.range(1, 1 + i);
            });
            assertTrue(actions.isEmpty());
            assertEquals("[1, 1, 2, 1, 2, 3]", c.toList().toString());
            assertIterableEquals(List.of("flatmap 1", "flatmap 2", "flatmap 3"), actions);
        }
    }

    void testHead() {
        assertEquals(2, $.range(2, 7).head().orElseThrow(IllegalStateException::new));
        assertNull($.just(null, 1, 2).head().orElse(null));
        assertNull($.empty().head().orElse(null));
    }

    void testFirst() {
        assertEquals(2, $.range(2, 7).first().orElseThrow(IllegalStateException::new));
        assertNull($.just(null, 1, 2).first().orElse(null));
        assertNull($.empty().first().orElse(null));
    }

    void testFilter2() {
        assertEquals("[2, 4, 6]", $.range(2, 7).filter((it, index) -> index % 2 == 0).toList().toString());
        assertEquals("[3, 5]", $.range(2, 7).filter((it, index) -> index % 2 == 1).toList().toString());

        assertThrows(NullPointerException.class, () -> $.range(2, 7).filter((ObjIntPredicate<Integer>) null));

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.range(2, 7).filter((i, index) -> {
                actions.add(String.format("filter %d at %d", i, index));
                return i % 2 == 0;
            });
            assertTrue(actions.isEmpty());
            assertEquals("[2, 4, 6]", c.toList().toString());
            assertIterableEquals(List.of(
                    "filter 2 at 0", "filter 3 at 1", "filter 4 at 2", "filter 5 at 3", "filter 6 at 4"
            ), actions);
        }
    }

    void testFilter() {
        assertEquals("[2, 4, 6]", $.range(2, 7).filter(i -> i % 2 == 0).toList().toString());
        assertEquals("[3, 5]", $.range(2, 7).filter(i -> i % 2 == 1).toList().toString());

        assertThrows(NullPointerException.class, () -> $.range(2, 7).filter((Predicate<Integer>) null));

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.range(2, 7).filter(i -> {
                actions.add("filter " + i);
                return i % 2 == 0;
            });
            assertTrue(actions.isEmpty());
            assertEquals("[2, 4, 6]", c.toList().toString());
            assertIterableEquals(List.of("filter 2", "filter 3", "filter 4", "filter 5", "filter 6"), actions);
        }
    }

    void testConcat() {
        assertEquals("[1, 2, 3, 4, 5]", $.just(1, 2).concat(List.of(3, 4, 5)).toList().toString());
        assertEquals("[1, 2]", $.just(1, 2).concat(null).toList().toString());
        assertEquals("[1, 2]", $.just(1, 2).concat(List.of()).toList().toString());

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.just(1, 2).concat(() -> new Iterator<>() {
                int i = 3;

                @Override
                public boolean hasNext() {
                    actions.add(String.format("check next %d", i));
                    return i < 6;
                }

                @Override
                public Integer next() {
                    actions.add(String.format("get next %d", i));
                    if (i < 6) return i++;
                    throw new NoSuchElementException();
                }
            });
            assertTrue(actions.isEmpty());
            assertEquals("[1, 2, 3, 4, 5]", c.toList().toString());
            assertIterableEquals(List.of(
                    "check next 3",
                    "get next 3",
                    "check next 4",
                    "get next 4",
                    "check next 5",
                    "get next 5",
                    "check next 6"
            ), actions);
        }
    }

    void testCompact() {
        assertEquals("[]", $.just(false, "", null, 0, 0.0, 0L, 0.0F).compact().toList().toString());

        // laziness
        {
            var actions = new ArrayList<String>();
            var c = $.infinite(0)
                    .map(it -> {
                        actions.add(String.format("map %d", it));
                        return it;
                    })
                    .compact()
                    .take(3);
            assertTrue(actions.isEmpty());
            assertEquals("[1, 2, 3]", c.toList().toString());
        }
    }

    void testChunk() {
        // chunk(3) - 1
        {
            var counter = new Counter(0);
            $.infinite(0).chunk(3).take(5).forEach((chunk, i) -> {
                assertIterableEquals(List.of(i * 3, i * 3 + 1, i * 3 + 2), chunk);
                assertEquals(i, counter.getAndIncr());
            });
            assertEquals(5, counter.get());
        }

        // chunk(3) - 2
        {
            var counter = new Counter(0);
            $.infinite(0).take(7).chunk(3).forEach((chunk, i) -> {
                switch (i) {
                    case 0 -> assertIterableEquals(List.of(0, 1, 2), chunk);
                    case 1 -> assertIterableEquals(List.of(3, 4, 5), chunk);
                    case 2 -> assertIterableEquals(List.of(6), chunk);
                    default -> throw new IllegalArgumentException("No such chunk " + i);
                }
                assertEquals(i, counter.getAndIncr());
            });
            assertEquals(3, counter.get());
        }

        // chunk(1)
        {
            var counter = new Counter(0);
            $.infinite(0).chunk(1).take(5).forEach((chunk, i) -> {
                assertIterableEquals(List.of(i), chunk);
                assertEquals(i, counter.getAndIncr());
            });
            assertEquals(5, counter.get());
        }

        assertThrows(IllegalArgumentException.class, () -> $.infinite(0).chunk(0));
        assertThrows(IllegalArgumentException.class, () -> $.infinite(0).chunk(-1));

        // laziness
        {
            var actions = new ArrayList<String>();
            var chain = $.infinite(0)
                    .take(7)
                    .chunk(3)
                    .map((it, i) -> {
                        actions.add(String.format("%d) chunk size %d", i, it.size()));
                        return it.size();
                    });
            assertTrue(actions.isEmpty());

            assertEquals("[3, 3, 1]", chain.toList().toString());
            assertIterableEquals(List.of(
                    "0) chunk size 3",
                    "1) chunk size 3",
                    "2) chunk size 1"
            ), actions);
        }
    }

    void testTake() {
        assertIterableEquals(List.of(0, 1, 2, 3, 4), $.infinite(0).take(5).toList());
        assertIterableEquals(List.of(0, 1, 2, 3, 4), $.just(0, 1, 2, 3, 4).take(100).toList());

        assertIterableEquals(List.of(), $.infinite(0).take(0).toList());

        assertIterableEquals(List.of(), $.infinite(0).take(-1).toList());

        // laziness
        {
            var actions = new ArrayList<String>();
            var chain = $.infinite(0)
                    .map(it -> {
                        actions.add(String.format("1) %d + %d", it, 3));
                        return it + 3;
                    })
                    .map(it -> {
                        actions.add(String.format("2) %d × %d", it, 2));
                        return it * 2;
                    })
                    .take(3);
            assertTrue(actions.isEmpty());

            assertIterableEquals(List.of(6, 8, 10), chain.toList());
            assertIterableEquals(List.of(
                    "1) 0 + 3", "2) 3 × 2",
                    "1) 1 + 3", "2) 4 × 2",
                    "1) 2 + 3", "2) 5 × 2"
            ), actions);
        }
    }

    void testZip() {

        assertEquals("[(1, 1), (2, 2), (3, null)]", $.just(1, 2, 3).zip(List.of(1, 2)).toList().toString());
        assertEquals("[(1, 1), (2, 2), (3, 3)]", $.just(1, 2, 3).zip(List.of(1, 2, 3)).toList().toString());
        assertEquals("[(1, 1), (2, 2), (3, 3), (null, 4), (null, 5)]", $.just(1, 2, 3).zip(List.of(1, 2, 3, 4, 5)).toList().toString());

        assertEquals("[(1, null), (2, null), (3, null)]", $.just(1, 2, 3).zip(List.of()).toList().toString());
        assertEquals("[(1, null), (2, null), (3, null)]", $.just(1, 2, 3).zip(null).toList().toString());

        assertEquals("[2, 4, 3]", $.just(1, 2, 3).zip(List.of(1, 2), (l, r) -> (l == null ? 0 : l) + (r == null ? 0 : r)).toList().toString());

        assertThrows(NullPointerException.class, () -> $(List.of(1)).zip(List.of(2), null));

        assertEquals($((List<Integer>) null).zip(null).toList().size(), 0);
    }

}
