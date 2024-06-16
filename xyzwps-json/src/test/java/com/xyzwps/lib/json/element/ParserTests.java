package com.xyzwps.lib.json.element;

import com.xyzwps.lib.json.util.CharGenerator;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

class ParserTests {

    @Test
    void simpleParserDemo() {
        var parser = new SimpleParser();
        Consumer<String> print = (str) -> System.out.println(parser.parse(CharGenerator.from(str)));

        print.accept("{}");
        print.accept("[]");
        print.accept("true");
        print.accept("false");
        print.accept("null");
        print.accept("123");
        print.accept("123.1E+10");

        print.accept("[false]");
        print.accept("""
                [false,"a",true]
                """);
        print.accept("""
                [false,["a",true]]
                """);

        print.accept("""
                {"a":true}
                """);
        print.accept("""
                {"a":true,"b":"b"}
                """);

        print.accept("""
                {
                  "a": {},
                  "b": { "b": "b" },
                  "c": [ "c", true ]
                }
                """);
    }


    @Test
    void stackParserDemo() {
        var parser = new StackParser();
        Consumer<String> print = (str) -> System.out.println(parser.parse(CharGenerator.from(str)));

        print.accept("{}");
        print.accept("[]");
        print.accept("true");
        print.accept("false");
        print.accept("null");
        print.accept("123");
        print.accept("123.1E+10");

        print.accept("[false]");
        print.accept("""
                [false,"a",true]
                """);
        print.accept("""
                [false,["a",true]]
                """);
        print.accept("""
                [false,["a",true],["b",1.23],{ "b": "b" }]
                """);

        print.accept("""
                {"a":true}
                """);
        print.accept("""
                {"a":true,"b":"b"}
                """);

        print.accept("""
                {
                  "a": {},
                  "b": { "b": "b","c":{ "b": "b" } },
                  "c": [ "c", true ]
                }
                """);
    }
}
