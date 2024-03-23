package com.xyzwps.lib.dollar.chain;

import com.xyzwps.lib.dollar.util.Counter;
import com.xyzwps.lib.dollar.util.ObjIntPredicate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.xyzwps.lib.dollar.Direction.ASC;
import static com.xyzwps.lib.dollar.Direction.DESC;
import static com.xyzwps.lib.dollar.Dollar.$.*;
import static org.junit.jupiter.api.Assertions.*;

class ChainTests {

    /*


    @Test
    void value() {
        List<Integer> list = Chain.just(1, 2, 1, 3, 4).value();
        assertTrue(list instanceof ArrayList); // ArrayList preferred
        assertEquals("[1, 2, 1, 3, 4]", list.toString());
    }

    @Test
    void unique() {
        assertEquals("[1, 2, 3]", Chain.just(1, 2, 1, 3).unique().value().toString());
        assertEquals("[1, 2]", Chain.just(1, 2, 1).unique().value().toString());
    }

    @Test
    void uniqueBy() {
        assertEquals("[1, 2, 3]", Chain.just(1, 2, 1, 3, 4).uniqueBy(i -> i % 3).value().toString());
        assertEquals("[1.2, 2.3]", Chain.just(1.2, 2.3, 1.4).uniqueBy(Double::intValue).value().toString());
    }

    @Test
    void toSet() {
        Set<Integer> set = Chain.just(1, 2, 1, 3, 4).toSet();
        assertEquals(4, set.size());
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        assertTrue(set.contains(3));
        assertTrue(set.contains(4));
    }

    @Test
    void takeWhile() {
        assertEquals("[1, 2]", Chain.just(1, 2, 3, 4, 5).takeWhile(i -> i < 3).value().toString());

        // short circuit
        {
            var counter = new Counter(0);
            assertEquals("[1, 2]", Chain.just(1, 2, 3, 4, 5).map(it -> {
                counter.incrAndGet();
                return it;
            }).takeWhile(i -> i < 3).value().toString());
            assertEquals(3, counter.get());
        }
    }

    @Test
    void take() {
        assertEquals("[1, 2]", Chain.just(1, 2, 3, 4).take(2).value().toString());
        assertEquals("[1, 2, 3, 4, 5]", Chain.just(1, 2, 3, 4, 5).take(6).value().toString());
        assertEquals("[1, 2, 3]", Chain.just(1, 2, 3, 4, 5).take(3).value().toString());

        // short circuit
        {
            var counter = new Counter(0);
            assertEquals("[1, 2, 3]", Chain.just(1, 2, 3, 4, 5).map(it -> {
                counter.incrAndGet();
                return it;
            }).take(3).value().toString());
            assertEquals(3, counter.get());
        }

        for (int i = 0; i > -10; i--) {
            final int takeCount = i;
            assertThrows(IllegalArgumentException.class, () -> Chain.just(1, 2).take(takeCount).value());
        }
    }

    @Test
    void skip() {
        assertEquals(Chain.just(1, 2, 3, 4).skip(-1).value().toString(), "[1, 2, 3, 4]");
        assertEquals(Chain.just(1, 2, 3, 4).skip(0).value().toString(), "[1, 2, 3, 4]");
        assertEquals(Chain.just(1, 2, 3, 4).skip(1).value().toString(), "[2, 3, 4]");
        assertEquals(Chain.just(1, 2, 3, 4).skip(2).value().toString(), "[3, 4]");
        assertEquals(Chain.just(1, 2, 3, 4).skip(3).value().toString(), "[4]");
        assertEquals(Chain.just(1, 2, 3, 4).skip(4).value().toString(), "[]");
        assertEquals(Chain.just(1, 2, 3, 4).skip(5).value().toString(), "[]");
    }

    @Test
    void skipWhile() {
        assertEquals(Chain.just(1, 0, 0, 0).skipWhile(i -> i < 1).value().toString(), "[1, 0, 0, 0]");
        assertEquals(Chain.just(1, 2, 0, 0).skipWhile(i -> i < 2).value().toString(), "[2, 0, 0]");
        assertEquals(Chain.just(1, 2, 3, 0).skipWhile(i -> i < 3).value().toString(), "[3, 0]");
        assertEquals(Chain.just(1, 2, 3, 4).skipWhile(i -> i < 4).value().toString(), "[4]");
        assertEquals(Chain.just(1, 2, 3, 4).skipWhile(i -> i < 5).value().toString(), "[]");

        assertThrows(NullPointerException.class, () -> Chain.just(1, 2).skipWhile(null).value());
    }

    @Test
    void reverse() {
        assertEquals("[3, 2, 1]", Chain.just(1, 2, 3).reverse().value().toString());
        assertEquals("[4, 3, 2, 1]", Chain.just(1, 2, 3, 4).reverse().value().toString());
    }

    @Test
    void reduce() {
        assertEquals(16, Chain.just(1, 2, 3).reduce(10, Integer::sum));
        assertEquals(20, Chain.just(1, 2, 3, 4).reduce(10, Integer::sum));

        {
            BiFunction<List<Integer>, Integer, List<Integer>> reducer = (list, it) -> {
                list.add(it);
                return list;
            };
            assertEquals("[1, 2, 3]", Chain.just(1, 2, 3).reduce(new ArrayList<>(), reducer).toString());
        }
    }

    @Test
    void orderBy() {
        assertEquals("[1, 2, 3, 4, 5]", Chain.just(1, 3, 5, 2, 4).orderBy(Function.identity(), ASC).value().toString());
        assertEquals("[5, 4, 3, 2, 1]", Chain.just(1, 3, 5, 2, 4).orderBy(Function.identity(), DESC).value().toString());

        assertEquals("[C1, A2, B3]", Chain.just("C1", "A2", "B3").orderBy(it -> Integer.parseInt(it.substring(1)), ASC).value().toString());
        assertEquals("[A2, B3, C1]", Chain.just("C1", "A2", "B3").orderBy(Function.identity(), ASC).value().toString());
    }

    @Test
    void map1() {
        assertEquals("[2, 4, 6]", Chain.just(1, 2, 3).map(i -> i * 2).value().toString());
        assertEquals("[1, 0, 1]", Chain.just(1, 2, 3).map(i -> i % 2).value().toString());
    }

    @Test
    void map2() {
        assertEquals("[11, 22, 33]", Chain.just(1, 2, 3).map((it, i) -> it + 10 * (i + 1)).value().toString());
    }

    @Test
    void keyBy() {
        {
            Map<Integer, Integer> map = Chain.just(1, 4, 7, 2, 5, 3).keyBy(i -> i % 3).value();
            assertEquals(3, map.size());
            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(0));
        }
        {
            Map<String, Integer> map = Chain.just(1, 2, 3, 4, 5).keyBy(i -> i % 2 == 0 ? "even" : "odd").value();
            assertEquals(2, map.size());
            assertEquals(1, map.get("odd"));
            assertEquals(2, map.get("even"));
        }
    }

    @Test
    void join() {
        assertEquals("hellonullworld", Chain.just("hello", "world").join(null));
        assertEquals("hello, world", Chain.just("hello", "world").join(", "));
        assertEquals("hello, null, world", Chain.just("hello", null, "world").join(", "));
        assertEquals("1 - 2 - 3 - 4 - 5", Chain.just(1, 2, 3, 4, 5).join(" - "));
    }

    @Test
    void groupBy() {
        {
            Map<Integer, List<Integer>> map = Chain.just(1, 4, 7, 2, 5, 3).groupBy(i -> i % 3).value();
            assertEquals(3, map.size());
            assertEquals("[1, 4, 7]", map.get(1).toString());
            assertEquals("[2, 5]", map.get(2).toString());
            assertEquals("[3]", map.get(0).toString());
        }

        {
            Map<String, List<Integer>> map = Chain.just(1, 2, 3, 4, 5).groupBy(i -> i % 2 == 0 ? "even" : "odd").value();
            assertEquals(2, map.size());
            assertEquals("[1, 3, 5]", map.get("odd").toString());
            assertEquals("[2, 4]", map.get("even").toString());
        }
    }

    @Test
    void forEach1() {
        List<Integer> t = new ArrayList<>();
        Chain.just(1, 2, 3).forEach(i -> t.add(i));
        assertEquals("[1, 2, 3]", t.toString());
    }

    @Test
    void forEach2() {
        List<Integer> t = new ArrayList<>();
        Chain.just(1, 2, 3).forEach((it, index) -> t.add(it + (index + 1) * 10));
        assertEquals("[11, 22, 33]", t.toString());
    }

    @Test
    void flatMap() {
        assertEquals(
                "[11, 12, 21, 22]",
                Chain.just(1, 2).flatMap(i -> listOf(i * 10 + 1, i * 10 + 2)).value().toString()
        );
        assertEquals(
                "[2, 3, 4, 6, 6, 9]",
                Chain.just(1, 2, 3).flatMap(i -> listOf(i * 2, i * 3)).value().toString()
        );
    }

    @Test
    void first() {
        assertEquals(Optional.of(1), Chain.just(1, 2).first());
        assertEquals(Optional.empty(), Chain.just(null, 2).first());
        assertEquals(Optional.empty(), Chain.empty().head());
    }

    @Test
    void filter1() {
        assertEquals("[a,  ]", Chain.just("a", " ", null).filter(Objects::nonNull).value().toString());
        assertEquals("[2, 4]", Chain.just(1, 2, 3, 4, 5).filter(i -> i % 2 == 0).value().toString());
        assertEquals("[1, 3, 5]", Chain.just(1, 2, 3, 4, 5).filter(i -> i % 2 == 1).value().toString());

        assertThrows(NullPointerException.class, () -> Chain.just(1, 2, 3, 4, 5).filter((Predicate<Integer>) null).value());
    }

    @Test
    void filter2() {
        assertEquals("[1, 3, 5]", Chain.just(1, 2, 3, 4, 5).filter((it, i) -> i % 2 == 0).value().toString());
        assertThrows(NullPointerException.class, () -> Chain.just(1, 2, 3, 4, 5).filter((ObjIntPredicate<Integer>) null).value().toString());
    }

    @Test
    void concat() {
        assertEquals("[a, , null, 1, 2, null, b]", Chain
                .just("a", "", null)
                .concat(null)
                .concat(listOf("1", "2"))
                .concat(listOf())
                .concat(listOf(null, "b"))
                .value().toString());
        assertEquals("[1, 2, 3, 4]", Chain.just(1, 2).concat(listOf(3, 4)).value().toString());
        assertEquals("[1, 2, 3, 4]", Chain.just(1, 2).concat(null).concat(listOf(3, 4)).value().toString());
    }

    @Test
    void compact() {
        assertEquals("[a]", Chain.just("a", "", null).compact().value().toString());
        assertEquals("[]", Chain.just(null, "", false, 0).compact().value().toString());
        assertEquals("[6, 哈哈]", Chain.just(null, 6, "", "哈哈", false, 0).compact().value().toString());
        assertEquals("[]", Chain.just((Object) null).compact().value().toString());

        assertEquals("[1, true, a]", Chain.just(null, 1, 0, true, false, "a", "").compact().value().toString());
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
        List<Integer> list = listOf(1, 2, 3, 4, 5, 6);
        for (int i = 1; i < cases.length; i++) {
            assertEquals(cases[i], Chain.from(list).chunk(i).value().toString());
        }

        assertThrows(IllegalArgumentException.class, () -> Chain.from(list).chunk(0).value());

        assertEquals("[[1, 2], [3, 4], [5]]", Chain.just(1, 2, 3, 4, 5).chunk(2).value().toString());
    }

    @Nested
    class ConstructListStage {
        @Test
        void nullList() {
            assertEquals(0, Chain.from((List<Object>) null).value().size());
        }

        @Test
        void just() {
            assertEquals(1, Chain.just(1).value().size());
            assertEquals(2, Chain.just(1, 1).value().size());
            assertEquals(3, Chain.just(1, 1, 1).value().size());
            assertEquals(4, Chain.just(1, 1, 1, 1).value().size());

            assertEquals(1, Chain.just(1).reduce(0, Integer::sum));
            assertEquals(2, Chain.just(1, 1).reduce(0, Integer::sum));
            assertEquals(3, Chain.just(1, 1, 1).reduce(0, Integer::sum));
            assertEquals(4, Chain.just(1, 1, 1, 1).reduce(0, Integer::sum));
        }

        @Test
        void range() {
            assertEquals("[1, 2, 3]", Chain.range(1, 4).value().toString());
        }
    }
    */
}
