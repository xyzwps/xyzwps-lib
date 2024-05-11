package com.xyzwps.lib.dollar;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;
import static com.xyzwps.lib.dollar.Direction.*;

class DollarTests {

    @Test
    void test() {


        assertEquals($.arrayList(1, 2, 3).toString(), "[1, 2, 3]");
        assertEquals($.chunk($.arrayList(1, 2, 3), 2).toString(), "[[1, 2], [3]]");
        assertEquals($.compact($.arrayList(null, 0)).toString(), "[]");
        assertEquals($.concat($.arrayList(1, 2), $.arrayList(3)).toString(), "[1, 2, 3]");
        assertEquals($.defaultTo(null, ""), "");
        assertEquals($.empty().size(), 0);
        assertEquals($.filter($.arrayList(1, 2, 3, 4), it -> it % 2 == 0).toString(), "[2, 4]");
        assertEquals($.filter($.arrayList(1, 2, 3, 4), (it, i) -> i % 2 == 0).toString(), "[1, 3]");
        assertEquals($.first($.arrayList(1, 2)).orElseThrow(), 1);
        assertEquals($.flatMap($.arrayList(1), it -> $.arrayList(it, it + 1)).toString(), "[1, 2]");
//        assertEquals($.flatMap($.arrayList(1), (it, i) -> $.arrayList(it, it + 1)).toString(), "[1, 2]");

        assertEquals($.groupBy($.arrayList(1), Function.identity()).toString(), "{1=[1]}");
        assertEquals($.head($.arrayList(1, 2)).orElseThrow(), 1);
        assertEquals($.hashMap().size(), 0);
        assertEquals($.hashMap(1, 1).size(), 1);
        assertEquals($.hashMap(1, 1, 2, 2).size(), 2);
        assertEquals($.hashMap(1, 1, 2, 2, 3, 3).size(), 3);
        assertEquals($.hashMap(1, 1, 2, 2, 3, 3, 4, 4).size(), 4);
        assertEquals($.hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5).size(), 5);
        assertEquals($.hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6).size(), 6);
        assertEquals($.hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7).size(), 7);
        assertEquals($.hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8).size(), 8);
        assertEquals($.hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9).size(), 9);
        assertEquals($.hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10).size(), 10);
        assertEquals($.just(1).size(), 1);
        assertEquals($.keyBy($.arrayList(1), Function.identity()).toString(), "{1=1}");
        assertEquals($.last($.arrayList(1, 2, 3)).orElseThrow(), 3);
        assertEquals($.length("123"), 3);
        assertEquals($.listFrom($.range(1, 4).iterator()).toString(), "[1, 2, 3]");
        assertEquals($.map($.arrayList(1, 2, 3), it -> it + 1).toString(), "[2, 3, 4]");
        assertEquals($.map($.arrayList(1, 2, 3), Integer::sum).toString(), "[1, 3, 5]");
        assertEquals($.mapKeys($.hashMap(1, 1), k -> k + 1).toString(), "{2=1}");
        assertEquals($.mapKeys($.hashMap(1, 1), Integer::sum).toString(), "{2=1}");
        assertEquals($.mapValues($.hashMap(1, 1), v -> v + 1).toString(), "{1=2}");
        assertEquals($.mapValues($.hashMap(1, 1), Integer::sum).toString(), "{1=2}");
        assertEquals($.orderBy($.arrayList(2, 1), Function.identity(), ASC).toString(), "[1, 2]");
        assertEquals($.pad("123", 5, "-"), "-123-");
        assertEquals($.padEnd("123", 5, "-"), "123--");
        assertEquals($.padStart("123", 5, "-"), "--123");
        assertEquals($.range(1, 4).size(), 3);
        assertEquals($.reduce($.arrayList(1, 2, 3), 0, Integer::sum), 6);
        assertEquals($.reduce($.hashMap(1, 1, 2, 2), 0, (r, k, v) -> r + k + v), 6);
        assertEquals($.reverse($.arrayList(1, 2, 3)).toString(), "[3, 2, 1]");
        assertEquals($.size($.arrayList(1, 2)), 2);
        assertEquals($.size($.hashMap(1, 1, 2, 2)), 2);
        assertEquals($.tail($.arrayList(1, 2, 3)).orElseThrow(), 3);
        assertEquals($.take("12345", 3), "123");
        assertEquals($.take($.arrayList(1, 2, 3, 4, 5), 3).toString(), "[1, 2, 3]");
        assertEquals($.takeRight("12345", 3), "345");
//        assertEquals($.takeRight($.arrayList(1, 2, 3, 4, 5), 3).toString(), "[1, 2, 3]");
        assertEquals($.takeWhile($.arrayList(1, 2, 3), it -> it <= 2).toString(), "[1, 2]");
        assertEquals($.toSet($.arrayList(1, 1)).toString(), "[1]");
        assertEquals($.unique($.arrayList(1, 1, 2)).toString(), "[1, 2]");
        assertEquals($.uniqueBy($.arrayList(1, 1, 2), Function.identity()).toString(), "[1, 2]");
        assertEquals($.uniqueBy($.arrayList(1, 3, 2), Integer::sum).toString(), "[1, 3]");
        assertEquals($.zip($.arrayList(1), $.arrayList(2)).toString(), "[(1, 2)]");
        assertEquals($.zip($.arrayList(1), $.arrayList(2), Integer::sum).toString(), "[3]");


        assertFalse($.isEmpty("刻晴"));
        assertFalse($.isEmpty($.arrayList(1)));
        assertFalse($.isEmpty($.hashMap(1, 1)));
        assertFalse($.isFalsey("刻晴"));
        assertFalse($.isNotEmpty(""));
        assertFalse($.isNotEmpty($.arrayList()));
        assertFalse($.isNotEmpty($.hashMap()));

        $.forEach($.arrayList(), it -> {
        });
        $.forEach($.arrayList(), (it, i) -> {
        });
    }
}
