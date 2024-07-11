package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.seq.Seq;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.xyzwps.lib.dollar.Dollar.*;
import static org.junit.jupiter.api.Assertions.*;

class LazinessTests {

    @Test
    void forTake() {
        List<String> trace = $.arrayList();

        Seq<Integer> stages = $.just(1, 2, 3, 4, 5, 6, 7)
                .map(i -> {
                    trace.add(String.format("map %d to %d", i, i + 2));
                    return i + 2;
                })
                .take(5)
                .filter(i -> {
                    trace.add(String.format("filter %d \\ 2 == 1", i));
                    return i % 2 == 1;
                })
                .map(i -> {
                    trace.add(String.format("map %d to %d", i, i - 2));
                    return i - 2;
                });

        assertTrue(trace.isEmpty());

        assertIterableEquals($.arrayList(1, 3, 5), stages.toList());
        assertIterableEquals(
                $.arrayList(
                        "map 1 to 3",
                        "filter 3 \\ 2 == 1",
                        "map 3 to 1",

                        "map 2 to 4",
                        "filter 4 \\ 2 == 1",

                        "map 3 to 5",
                        "filter 5 \\ 2 == 1",
                        "map 5 to 3",

                        "map 4 to 6",
                        "filter 6 \\ 2 == 1",

                        "map 5 to 7",
                        "filter 7 \\ 2 == 1",
                        "map 7 to 5"
                ),
                trace
        );

    }
}
