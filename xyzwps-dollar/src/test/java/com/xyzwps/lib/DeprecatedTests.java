package com.xyzwps.lib;

import com.xyzwps.lib.dollar.Direction;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.xyzwps.lib.dollar.Dollar.*;
import static org.junit.jupiter.api.Assertions.*;

class DeprecatedTests {

    @Test
    void filter() {
        assertEquals("[1, 3, 5]",
                $.just(1, 2, 3, 4, 5).filter(i -> i % 2 == 1).toList().toString());
        assertEquals("[2, 4]",
                $.just(1, 2, 3, 4, 5).filter(i -> i % 2 == 0).toList().toString());
    }

    @Test
    void first() {
        assertEquals(Optional.of(1), $.just(1, 3).first());
        assertEquals(Optional.empty(), $.just((Object) null).first());
        assertEquals(Optional.empty(), $.empty().first());
    }

    @Test
    void flatMap() {
        assertEquals("[2, 3, 4, 6, 6, 9]", $.just(1, 2, 3).flatMap(i -> $.just(i * 2, i * 3).toList()).toList().toString());
        assertEquals("[]", $.just(1, 2, 3).flatMap(i -> null).toList().toString());
    }

    @Test
    void groupBy() {
        Map<String, List<Integer>> map = $.just(1, 2, 3, 4, 5).groupBy(i -> i % 2 == 0 ? "even" : "odd").toMap();
        assertEquals("[1, 3, 5]", map.get("odd").toString());
        assertEquals("[2, 4]", map.get("even").toString());
        assertEquals(2, map.size());
    }

    @Test
    void join() {
        assertEquals("hello, world", $.just("hello", "world").join(", "));
        assertEquals("1 - 2 - 3 - 4 - 5", $.just(1, 2, 3, 4, 5).join(" - "));
    }

    @Test
    void keyBy() {
        Map<String, Integer> map = $.just(1, 2, 3, 4, 5).keyBy(i -> i % 2 == 0 ? "even" : "odd").toMap();
        assertEquals(1, map.get("odd"));
        assertEquals(2, map.get("even"));
        assertEquals(2, map.size());
    }

    @Test
    void map() {
        assertEquals("[2, 4, 6]", $.just(1, 2, 3).map(i -> i * 2).toList().toString());
        assertEquals("[1, 0, 1]", $.just(1, 2, 3).map(i -> i % 2).toList().toString());
    }

    @Test
    void orderBy() {
        assertEquals("[C1, A2, B3]",
                $.just("C1", "A2", "B3").orderBy(it -> Integer.parseInt(it.substring(1)), Direction.ASC).toList().toString());
        assertEquals("[A2, B3, C1]",
                $.just("C1", "A2", "B3").orderBy(Function.identity(), Direction.ASC).toList().toString());
    }

    @Test
    void reduce() {
        assertEquals(16, $.just(1, 2, 3).reduce(10, Integer::sum));
        BiFunction<ArrayList<Integer>, Integer, ArrayList<Integer>> accelerator = (list, it) -> {
            list.add(it);
            return list;
        };
//        assertEquals("[1, 2, 3]", $.just(1, 2, 3).reduce(new ArrayList<Integer>(), accelerator).toString());
    }

    @Test
    void reverse() {
        assertEquals("[3, 2, 1]", $.just(1, 2, 3).reverse().toList().toString());
    }

    @Test
    void take() {
        assertEquals("[1, 2, 3, 4, 5]", $.just(1, 2, 3, 4, 5).take(6).toList().toString());
        assertEquals("[1, 2, 3]", $.just(1, 2, 3, 4, 5).take(3).toList().toString());
    }

    @Test
    void takeWhile() {
        assertEquals("[1, 2]", $.just(1, 2, 3, 4, 5).takeWhile(i -> i < 3).toList().toString());
    }

    @Test
    void unique() {
        assertEquals("[1, 2]", $.just(1, 2, 1).unique().toList().toString());
    }

    @Test
    void uniqueBy() {
        assertEquals("[1.2, 2.3]", $.just(1.2, 2.3, 1.4).uniqueBy(Double::intValue).toList().toString());
    }
}
