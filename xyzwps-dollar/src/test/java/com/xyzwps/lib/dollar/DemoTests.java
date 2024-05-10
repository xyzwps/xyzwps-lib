package com.xyzwps.lib.dollar;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.xyzwps.lib.dollar.Dollar.*;
import static org.junit.jupiter.api.Assertions.*;

class DemoTests {

    @Test
    void chainListOpts() {
        List<String> log = new ArrayList<>();
        List<Integer> s = Arrays.asList(2, 3, 4, 5, 6);

        List<Integer> result = $(s)
                .map(i -> {
                    int r = i * 2;
                    log.add(String.format("1) map %d to %d", i, r));
                    return r;
                })
                .flatMap(i -> {
                    int t1 = i, t2 = i + 2;
                    log.add(String.format("  2) flat map %d to %d, %d", i, t1, t2));
                    return $.just(t1, t2).toList();
                })
                .orderBy(Function.identity(), Direction.DESC)
                .filter(i -> {
                    boolean r = i > 6;
                    log.add(String.format("    3) filter %d and %s it", i, r ? "keep" : "DROP"));
                    return r;
                })
                .map(i -> {
                    int r = i * 2;
                    log.add(String.format("      4) %d ====> %d", i, r));
                    return r;
                })
                .unique()
                .toList();

        assertEquals("[28, 24, 20, 16]", result.toString());

        List<String> logRows = new ArrayList<>();
        logRows.add("1) map 2 to 4");
        logRows.add("  2) flat map 4 to 4, 6");
        logRows.add("1) map 3 to 6");
        logRows.add("  2) flat map 6 to 6, 8");
        logRows.add("1) map 4 to 8");
        logRows.add("  2) flat map 8 to 8, 10");
        logRows.add("1) map 5 to 10");
        logRows.add("  2) flat map 10 to 10, 12");
        logRows.add("1) map 6 to 12");
        logRows.add("  2) flat map 12 to 12, 14");
        logRows.add("    3) filter 14 and keep it");
        logRows.add("      4) 14 ====> 28");
        logRows.add("    3) filter 12 and keep it");
        logRows.add("      4) 12 ====> 24");
        logRows.add("    3) filter 12 and keep it");
        logRows.add("      4) 12 ====> 24");
        logRows.add("    3) filter 10 and keep it");
        logRows.add("      4) 10 ====> 20");
        logRows.add("    3) filter 10 and keep it");
        logRows.add("      4) 10 ====> 20");
        logRows.add("    3) filter 8 and keep it");
        logRows.add("      4) 8 ====> 16");
        logRows.add("    3) filter 8 and keep it");
        logRows.add("      4) 8 ====> 16");
        logRows.add("    3) filter 6 and DROP it");
        logRows.add("    3) filter 6 and DROP it");
        logRows.add("    3) filter 4 and DROP it");

        assertIterableEquals(logRows, log);
    }

    @Test
    void readMeDemo() {
        assertIterableEquals(
                $.arrayList(28, 24, 20, 16),
                $.just(2, 3, 4, 5, 6)
                        .map(i -> i * 2)
                        .flatMap(i -> $.just(i, i + 2).toList())
                        .orderBy(Function.identity(), Direction.DESC)
                        .filter(i -> i > 6)
                        .map(i -> i * 2)
                        .unique()
                        .toList()
        );
    }

}

