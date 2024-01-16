package com.xyzwps.lib;

import com.xyzwps.lib.dollar.ChainFactory;
import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.MapEntryChainFactory;
import com.xyzwps.lib.dollar.iterator.RangeIterable;
import com.xyzwps.lib.dollar.util.Counter;
import com.xyzwps.lib.dollar.util.ObjIntFunction;
import com.xyzwps.lib.dollar.util.ObjIntPredicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public record ChainCases(ChainFactory cf, MapEntryChainFactory mf) {

    public void test() {
        new TestChain().test();
        this.testMapEntryChain();
    }


    private void testMapEntryChain() {

    }

    class TestChain {

        void test() {
            testChunk();
            testCompact();
            testConcat();
            testFilter();
            testFilter2();
            testFirst();
            testFlatMap();
            testForEach();
            testForEach2();

            testHead();
            testIterator();
            testJoin();

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
        }

        /**
         * <K> MapEntryChain<K, List<T>> groupBy(Function<T, K> toKey);
         *
         * <K> MapEntryChain<K, T> keyBy(Function<T, K> toKey);
         * <p>
         * <K> Chain<T> uniqueBy(Function<T, K> toKey) {
         */

        void testUnique() {
            assertEquals("[1, 2, 3, 4]", cf.just(1, 2, 1, 2, 3, 1, 2, 3, 4).unique().toList().toString());

            // laziness
            {
                var actions = new ArrayList<String>();
                var c = cf.just(1, 2, 2, 3, 4, 1, 4)
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
            assertEquals("[]", cf.empty().toSet().toString());

            assertEquals("[]", cf.just().toSet().toString());
            {
                var set = cf.just(1, 2, 1).toSet();
                assertEquals(set.size(), 2);
                assertTrue(set.contains(1));
                assertTrue(set.contains(2));
            }

            assertEquals("[]", cf.from(null).toSet().toString());
            {
                var set = cf.from(List.of(1, 2, 1, 2)).toSet();
                assertEquals(set.size(), 2);
                assertTrue(set.contains(1));
                assertTrue(set.contains(2));
            }

            {
                var set = cf.range(1, 3).toSet();
                assertEquals(set.size(), 2);
                assertTrue(set.contains(1));
                assertTrue(set.contains(2));
            }
            assertEquals("[]", cf.range(1, 1).toSet().toString());
            {
                var set = cf.range(1, -3).toSet();
                assertEquals(set.size(), 4);
                assertTrue(set.contains(1));
                assertTrue(set.contains(0));
                assertTrue(set.contains(-1));
                assertTrue(set.contains(-2));
            }
        }

        void testToList() {
            assertEquals("[]", cf.empty().toList().toString());

            assertEquals("[]", cf.just().toList().toString());
            assertEquals("[1, 2]", cf.just(1, 2).toList().toString());

            assertEquals("[]", cf.from(null).toList().toString());
            assertEquals("[1, 2]", cf.from(List.of(1, 2)).toList().toString());

            assertEquals("[1, 2]", cf.range(1, 3).toList().toString());
            assertEquals("[]", cf.range(1, 1).toList().toString());
            assertEquals("[1, 0, -1, -2]", cf.range(1, -3).toList().toString());
        }

        void testTakeWhile() {
            assertEquals("[1, 2, 3]", cf.just(1, 2, 3, 4, 5).takeWhile(it -> it < 4).toList().toString());
            assertEquals("[]", cf.just(1, 2, 3, 4, 5).takeWhile(it -> it < 0).toList().toString());

            assertThrows(NullPointerException.class, () -> cf.just(1, 2, 3, 4, 5).takeWhile(null));

            // laziness
            {
                var actions = new ArrayList<String>();
                var c = cf.just(1, 2, 3, 4, 5)
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
            assertEquals("[4, 5]", cf.just(1, 2, 3, 4, 5).skipWhile(it -> it < 4).toList().toString());
            assertEquals("[]", cf.just(1, 2, 3, 4, 5).skipWhile(it -> it < 6).toList().toString());

            assertThrows(NullPointerException.class, () -> cf.just(1, 2, 3, 4, 5).skipWhile(null));

            // laziness
            {
                var actions = new ArrayList<String>();
                var c = cf.just(1, 2, 3, 4, 5)
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
            assertEquals("[4, 5]", cf.just(1, 2, 3, 4, 5).skip(3).toList().toString());
            assertEquals("[]", cf.just(1, 2, 3, 4, 5).skip(5).toList().toString());
            assertEquals("[1, 2, 3, 4, 5]", cf.just(1, 2, 3, 4, 5).skip(0).toList().toString());
            assertEquals("[1, 2, 3, 4, 5]", cf.just(1, 2, 3, 4, 5).skip(-1).toList().toString());

            // laziness
            {
                var actions = new ArrayList<String>();
                var c = cf.just(1, 2, 3, 4, 5)
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
            assertEquals(0, cf.just().size());
            assertEquals(3, cf.just(1, 2, 3).size());
        }

        void testReverse() {
            assertEquals("[1, 2, 3, 4, 5]", cf.just(5, 4, 3, 2, 1).reverse().toList().toString());

            // laziness
            var actions = new ArrayList<String>();
            var c = cf.just(1, 2, 3, 4, 5)
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
            assertEquals(15, cf.just(1, 2, 3, 4, 5).reduce(0, Integer::sum));

            assertThrows(NullPointerException.class, () -> cf.just(1, 2, 3, 4, 5).reduce(0, null));
        }

        void testOrderBy() {
            assertEquals("[1, 2, 3, 4, 5]", cf.just(1, 4, 3, 2, 5).orderBy(Function.identity(), Direction.ASC).toList().toString());
            assertEquals("[5, 4, 3, 2, 1]", cf.just(1, 4, 3, 2, 5).orderBy(Function.identity(), Direction.DESC).toList().toString());

            assertThrows(NullPointerException.class, () -> cf.just(1, 4, 3, 2, 5).orderBy(null, Direction.ASC));
            assertThrows(NullPointerException.class, () -> cf.just(1, 4, 3, 2, 5).orderBy(null, Direction.DESC));
            assertThrows(NullPointerException.class, () -> cf.just(1, 4, 3, 2, 5).orderBy(Function.identity(), null));

            // laziness
            {
                var actions = new ArrayList<String>();
                var c = cf.just(1.1, 4.1, 3.1, 2.1, 5.1)
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
            assertEquals("[1, 4, 9, 16]", cf.just(1, 2, 3, 4).map((it, i) -> it * (i + 1)).toList().toString());

            assertThrows(NullPointerException.class, () -> cf.just(1, 2, 3, 4).map((ObjIntFunction<Integer, Object>) null));

            // laziness
            {
                var actions = new ArrayList<String>();
                var c = cf.just(1, 2, 3, 4)
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
            assertEquals("[2, 4, 6, 8]", cf.just(1, 2, 3, 4).map(i -> i * 2).toList().toString());

            assertThrows(NullPointerException.class, () -> cf.just(1, 2, 3, 4).map((Function<Integer, Object>) null));

            // laziness
            {
                var actions = new ArrayList<String>();
                var c = cf.just(1, 2, 3, 4)
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
            assertEquals("1234", cf.just(1, 2, 3, 4).join(""));
            assertEquals("1-2-3-4", cf.just(1, 2, 3, 4).join("-"));
            assertThrows(NullPointerException.class, () -> cf.just(1, 2, 3, 4).join(null));
        }

        void testIterator() {
            {
                var itr = cf.just(1, 2, 3, 4).iterator();
                var counter = new Counter(1);
                while (itr.hasNext()) {
                    assertEquals(itr.next(), counter.getAndIncr());
                }
            }

            // laziness
            {
                var actions = new ArrayList<String>();
                var itr = cf.just(1, 2, 4)
                        .flatMap(i -> {
                            actions.add("flatmap " + i);
                            return new RangeIterable(i, i * 2);
                        })
                        .iterator();
                assertTrue(actions.isEmpty());
                var counter = new Counter(1);
                while (itr.hasNext()) {
                    assertEquals(itr.next(), counter.getAndIncr());
                }
                assertIterableEquals(List.of("flatmap 1", "flatmap 2", "flatmap 4"), actions);
            }
        }

        void testForEach2() {
            var list = new ArrayList<Integer>();
            cf.just(1, 2, 3).forEach((it, i) -> {
                list.add(i);
                list.add(it);
            });
            assertEquals("[0, 1, 1, 2, 2, 3]", list.toString());

            assertThrows(NullPointerException.class, () -> cf.just(1, 2, 3).forEach((ObjIntConsumer<Integer>) null));
        }

        void testForEach() {
            var list = new ArrayList<Integer>();
            cf.just(1, 2, 3).forEach(i -> list.add(i));
            assertEquals("[1, 2, 3]", list.toString());

            assertThrows(NullPointerException.class, () -> cf.just(1, 2, 3).forEach((Consumer<Integer>) null));
        }

        void testFlatMap() {
            assertEquals("[1, 1, 2, 1, 2, 3]", cf.just(1, 2, 3).flatMap(i -> new RangeIterable(1, 1 + i)).toList().toString());

            assertThrows(NullPointerException.class, () -> cf.just(1, 2, 3).flatMap(null));

            // laziness
            {
                var actions = new ArrayList<String>();
                var c = cf.just(1, 2, 3).flatMap(i -> {
                    actions.add("flatmap " + i);
                    return new RangeIterable(1, 1 + i);
                });
                assertTrue(actions.isEmpty());
                assertEquals("[1, 1, 2, 1, 2, 3]", c.toList().toString());
                assertIterableEquals(List.of("flatmap 1", "flatmap 2", "flatmap 3"), actions);
            }
        }

        void testHead() {
            assertEquals(2, cf.range(2, 7).head().orElseThrow(IllegalStateException::new));
            assertNull(cf.just(null, 1, 2).head().orElse(null));
            assertNull(cf.empty().head().orElse(null));
        }

        void testFirst() {
            assertEquals(2, cf.range(2, 7).first().orElseThrow(IllegalStateException::new));
            assertNull(cf.just(null, 1, 2).first().orElse(null));
            assertNull(cf.empty().first().orElse(null));
        }

        void testFilter2() {
            assertEquals("[2, 4, 6]", cf.range(2, 7).filter((it, index) -> index % 2 == 0).toList().toString());
            assertEquals("[3, 5]", cf.range(2, 7).filter((it, index) -> index % 2 == 1).toList().toString());

            assertThrows(NullPointerException.class, () -> cf.range(2, 7).filter((ObjIntPredicate<Integer>) null));

            // laziness
            {
                var actions = new ArrayList<String>();
                var c = cf.range(2, 7).filter((i, index) -> {
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
            assertEquals("[2, 4, 6]", cf.range(2, 7).filter(i -> i % 2 == 0).toList().toString());
            assertEquals("[3, 5]", cf.range(2, 7).filter(i -> i % 2 == 1).toList().toString());

            assertThrows(NullPointerException.class, () -> cf.range(2, 7).filter((Predicate<Integer>) null));

            // laziness
            {
                var actions = new ArrayList<String>();
                var c = cf.range(2, 7).filter(i -> {
                    actions.add("filter " + i);
                    return i % 2 == 0;
                });
                assertTrue(actions.isEmpty());
                assertEquals("[2, 4, 6]", c.toList().toString());
                assertIterableEquals(List.of("filter 2", "filter 3", "filter 4", "filter 5", "filter 6"), actions);
            }
        }

        void testConcat() {
            assertEquals("[1, 2, 3, 4, 5]", cf.just(1, 2).concat(List.of(3, 4, 5)).toList().toString());
            assertEquals("[1, 2]", cf.just(1, 2).concat(null).toList().toString());
            assertEquals("[1, 2]", cf.just(1, 2).concat(List.of()).toList().toString());

            // laziness
            {
                var actions = new ArrayList<String>();
                var c = cf.just(1, 2).concat(() -> new Iterator<>() {
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
            assertEquals("[]", cf.just(false, "", null, 0, 0.0, 0L, 0.0F).compact().toList().toString());

            // laziness
            {
                var actions = new ArrayList<String>();
                var c = cf.infinite(0)
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
                cf.infinite(0).chunk(3).take(5).forEach((chunk, i) -> {
                    assertIterableEquals(List.of(i * 3, i * 3 + 1, i * 3 + 2), chunk);
                    assertEquals(i, counter.getAndIncr());
                });
                assertEquals(5, counter.get());
            }

            // chunk(3) - 2
            {
                var counter = new Counter(0);
                cf.infinite(0).take(7).chunk(3).forEach((chunk, i) -> {
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
                cf.infinite(0).chunk(1).take(5).forEach((chunk, i) -> {
                    assertIterableEquals(List.of(i), chunk);
                    assertEquals(i, counter.getAndIncr());
                });
                assertEquals(5, counter.get());
            }

            assertThrows(IllegalArgumentException.class, () -> cf.infinite(0).chunk(0));
            assertThrows(IllegalArgumentException.class, () -> cf.infinite(0).chunk(-1));

            // laziness
            {
                var actions = new ArrayList<String>();
                var chain = cf.infinite(0)
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
            assertIterableEquals(List.of(0, 1, 2, 3, 4), cf.infinite(0).take(5).toList());
            assertIterableEquals(List.of(0, 1, 2, 3, 4), cf.just(0, 1, 2, 3, 4).take(100).toList());

            assertIterableEquals(List.of(), cf.infinite(0).take(0).toList());

            assertIterableEquals(List.of(), cf.infinite(0).take(-1).toList());

            // laziness
            {
                var actions = new ArrayList<String>();
                var chain = cf.infinite(0)
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


    }


}
