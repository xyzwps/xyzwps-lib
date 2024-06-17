package com.xyzwps.lib.json.token;

import com.xyzwps.lib.json.util.CharGenerator;
import com.xyzwps.lib.json.SyntaxException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleTokenizerTests {

    static SimpleTokenizer create(String text) {
        return new SimpleTokenizer(CharGenerator.from(text));
    }

    @Nested
    class TrueTests {
        @Test
        void ok() {
            List.of("true", " true", "true ", " true ").forEach(text -> {
                var it = create(text);
                assertEquals(it.nextToken(), BooleanToken.TRUE);
                assertNull(it.nextToken());
            });
        }

        @Test
        void invalid() {
            List.of("t", "tr", "tru", "t ", "t1", "tRUE").forEach(text -> {
                var it = create(text);
                assertThrows(SyntaxException.class, it::nextToken);
            });
        }
    }

    @Nested
    class FalseTests {
        @Test
        void ok() {
            List.of("false", " false", "false ", " false ").forEach(text -> {
                var it = create(text);
                assertEquals(it.nextToken(), BooleanToken.FALSE);
                assertNull(it.nextToken());
            });
        }

        @Test
        void invalid() {
            List.of("f", "fa", "fal", "fals ", "fx", "fALSE").forEach(text -> {
                var it = create(text);
                assertThrows(SyntaxException.class, it::nextToken);
            });
        }
    }

    @Nested
    class NullTests {
        @Test
        void ok() {
            List.of("null", " null", "null ", " null ").forEach(text -> {
                var it = create(text);
                assertEquals(it.nextToken(), NullToken.INSTANCE);
                assertNull(it.nextToken());
            });
        }

        @Test
        void invalid() {
            List.of("n", "nu", "nul", "nul ", "nx", "nULL").forEach(text -> {
                var it = create(text);
                assertThrows(SyntaxException.class, it::nextToken);
            });
        }
    }

    @Nested
    class IntegerTests {

        @Test
        void ok() {
            var cases = new ArrayList<Case<String, String>>();

            for (int i = 0; i < 100; i++) {
                // positive
                {
                    var expected = String.format("%d", i);
                    cases.add(new Case<>(String.format("%d", i), expected));
                    cases.add(new Case<>(String.format(" %d", i), expected));
                    cases.add(new Case<>(String.format("%d ", i), expected));
                    cases.add(new Case<>(String.format(" %d ", i), expected));
                }
                // negative
                {
                    var expected = String.format("-%d", i);
                    cases.add(new Case<>(String.format("-%d", i), expected));
                    cases.add(new Case<>(String.format(" -%d", i), expected));
                    cases.add(new Case<>(String.format("-%d ", i), expected));
                    cases.add(new Case<>(String.format(" -%d ", i), expected));
                }
            }

            for (long i = 100000000000L; i < 100000000020L; i++) {
                // positive
                {
                    var expected = String.format("%d", i);
                    cases.add(new Case<>(String.format("%d", i), expected));
                    cases.add(new Case<>(String.format(" %d", i), expected));
                    cases.add(new Case<>(String.format("%d ", i), expected));
                    cases.add(new Case<>(String.format(" %d ", i), expected));
                }
                // negative
                {
                    var expected = String.format("-%d", i);
                    cases.add(new Case<>(String.format("-%d", i), expected));
                    cases.add(new Case<>(String.format(" -%d", i), expected));
                    cases.add(new Case<>(String.format("-%d ", i), expected));
                    cases.add(new Case<>(String.format(" -%d ", i), expected));
                }
            }

            cases.forEach(c -> {
                var it = create(c.input);
                assertEquals(it.nextToken(), new IntegerToken(new BigInteger(c.expected)));
                assertNull(it.nextToken());
            });
        }

        @Test
        void invalid() {
            List.of(new Case<>("-", "0")).forEach(c -> {
                var it = create(c.input);
                assertThrows(SyntaxException.class, it::nextToken);
            });

            List.of(new Case<>("01", "0"), new Case<>("-01", "0"), new Case<>("0 1", "0")).forEach(c -> {
                var it = create(c.input);
                var nextToken = it.nextToken();
                assertNotNull(nextToken);
                assertEquals(nextToken, new IntegerToken(new BigInteger(c.expected)));
            });
        }
    }

    @Nested
    class DecimalWithoutExpoTests {

        @Test
        void ok() {
            var cases = new ArrayList<Case<String, String>>();

            for (int i = 0; i < 100; i++) {
                // x.0
                {
                    var expected = String.format("%d.0", i);
                    cases.add(new Case<>(String.format("%d.0", i), expected));
                    cases.add(new Case<>(String.format(" %d.0", i), expected));
                    cases.add(new Case<>(String.format("%d.0 ", i), expected));
                    cases.add(new Case<>(String.format(" %d.0 ", i), expected));
                }
                // x.00
                {
                    var expected = String.format("%d.00", i);
                    cases.add(new Case<>(String.format("%d.00", i), expected));
                    cases.add(new Case<>(String.format(" %d.00", i), expected));
                    cases.add(new Case<>(String.format("%d.00 ", i), expected));
                    cases.add(new Case<>(String.format(" %d.00 ", i), expected));
                }
                // x.123
                {
                    var expected = String.format("%d.123", i);
                    cases.add(new Case<>(String.format("%d.123", i), expected));
                    cases.add(new Case<>(String.format(" %d.123", i), expected));
                    cases.add(new Case<>(String.format("%d.123 ", i), expected));
                    cases.add(new Case<>(String.format(" %d.123 ", i), expected));
                }

                // -x.0
                {
                    var expected = String.format("-%d.0", i);
                    cases.add(new Case<>(String.format("-%d.0", i), expected));
                    cases.add(new Case<>(String.format(" -%d.0", i), expected));
                    cases.add(new Case<>(String.format("-%d.0 ", i), expected));
                    cases.add(new Case<>(String.format(" -%d.0 ", i), expected));
                }

                // -x.120
                {
                    var expected = String.format("-%d.120", i);
                    cases.add(new Case<>(String.format("-%d.120", i), expected));
                    cases.add(new Case<>(String.format(" -%d.120", i), expected));
                    cases.add(new Case<>(String.format("-%d.120 ", i), expected));
                    cases.add(new Case<>(String.format(" -%d.120 ", i), expected));
                }
            }

            cases.forEach(c -> {
                var it = create(c.input);
                assertEquals(it.nextToken(), new DecimalToken(new BigDecimal(c.expected)));
                assertNull(it.nextToken());
            });
        }

        @Test
        void invalid() {
            List.of(
                    new Case<>("-.", "0"),
                    new Case<>("-1.", "1"),
                    new Case<>("0.", "0")
            ).forEach(c -> {
                var it = create(c.input);
                assertThrows(SyntaxException.class, it::nextToken);
            });

            List.of(new Case<>("01", "1"), new Case<>("-01", "1")).forEach(c -> {
                var it = create(c.input);
                var nextToken = it.nextToken();
                assertNotNull(nextToken);
                assertEquals(nextToken, new IntegerToken(BigInteger.ZERO));
            });
        }
    }

    @Nested
    class DecimalWithExpoTests {

        @Test
        void ok() {
            List.of(
                    new Case<>("3.14E12", "3.14E12"),
                    new Case<>("3.14E+12", "3.14E12"),
                    new Case<>("3.14e12", "3.14E12"),
                    new Case<>("3.14E-12", "3.14E-12"),
                    new Case<>("-3.27e12", "-3.27e12")
            ).forEach(c -> {
                var it = create(c.input);
                assertEquals(it.nextToken(), new DecimalToken(new BigDecimal(c.expected)));
                assertNull(it.nextToken());
            });
        }

        @Test
        void invalid() {
            List.of(
                    new Case<>("3.14E", "0"),
                    new Case<>("3.14e+.", "1"),
                    new Case<>("3.14e-", "0")
            ).forEach(c -> {
                var it = create(c.input);
                assertThrows(SyntaxException.class, it::nextToken);
            });
        }
    }

    @Nested
    class StringTests {
        @Test
        void ok() {
            List.of(
                    new Case<>("\"abc\"", "abc"),
                    new Case<>("\"\\b\\t\\n\\r\\f\\\\\\\"\"", "\b\t\n\r\f\\\""),
                    new Case<>("\"\\u523b\\u6674\"", "刻晴")
            ).forEach(c -> {
                var it = create(c.input);
                assertEquals(it.nextToken(), new StringToken(c.expected));
                assertNull(it.nextToken());
            });
        }

        @Test
        void invalid() {
            List.of(new Case<>("\"", "0")).forEach(c -> {
                var it = create(c.input);
                assertThrows(SyntaxException.class, it::nextToken);
            });
        }
    }

    record Case<I, E>(I input, E expected) {
    }
}
