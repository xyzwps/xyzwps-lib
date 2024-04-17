package com.xyzwps.lib.json.element;


import com.xyzwps.lib.json.util.StringCharGenerator;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class VeryDeepJsonObjectTests {

    static final String VERY_DEEP_JSON;

    static {
        String start = "1";
        for (int i = 0; i < 10_0000; i++) {
            start = "{\"a\":" + start + "}";
        }
        VERY_DEEP_JSON = start;
    }

    @Test
    void simpleParserDemo() {
        var parser = new SimpleParser();
        Consumer<String> print = (str) -> System.out.println(parser.parse(new StringCharGenerator(str)).getClass().getCanonicalName());

        assertThrows(StackOverflowError.class, () -> print.accept(VERY_DEEP_JSON));
    }


    @Test
    void stackParserDemo() {
        var parser = new StackParser();
        Consumer<String> print = (str) -> System.out.println(parser.parse(new StringCharGenerator(str)).getClass().getCanonicalName());

        print.accept(VERY_DEEP_JSON);
    }
}
