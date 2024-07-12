package com.xyzwps.lib.dollar.util;

import org.junit.jupiter.api.Test;


import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

import static com.xyzwps.lib.dollar.Dollar.$.*;

class StringUtilsTests {

    @Test
    void testCamelCase() {
        assertNull(camelCase(null));
        assertEquals("", camelCase(""));
        assertEquals("fooBar", camelCase("foo_bar"));
        assertEquals("fooBar", camelCase("Foo Bar"));
        assertEquals("fooBar", camelCase("--foo-bar--"));
        assertEquals("fooBar", camelCase("__foo_Bar__"));
    }

    @Test
    void testIsEmpty() {
        final String nullString = null;

        assertTrue(isEmpty(nullString));
        assertTrue(isEmpty(""));

        assertFalse(isNotEmpty(nullString));
        assertFalse(isNotEmpty(""));
    }

    @Test
    void testLength() {
        assertEquals(0, length(null));
        assertEquals(0, length(""));
        assertEquals(2, length("  "));
        assertEquals(2, length("刻晴"));
    }

    @Test
    void testPad() {
        assertThrows(IllegalArgumentException.class, () -> pad("", -1, " "));

        assertEquals("      ", pad(null, 6, null));
        assertEquals("      ", pad(null, 6, ""));
        assertEquals("      ", pad(null, 6, " "));
        assertEquals("aaaaaa", pad(null, 6, "a"));
        assertEquals("ababab", pad(null, 6, "ab"));
        assertEquals("abcdab", pad(null, 6, "abcd"));

        assertEquals("      ", pad("", 6, null));
        assertEquals("      ", pad("", 6, ""));
        assertEquals("      ", pad("", 6, " "));
        assertEquals("aaaaaa", pad("", 6, "a"));
        assertEquals("ababab", pad("", 6, "ab"));
        assertEquals("abcdab", pad("", 6, "abcd"));

        assertEquals(" +++  ", pad("+++", 6, null));
        assertEquals(" +++  ", pad("+++", 6, ""));
        assertEquals(" +++  ", pad("+++", 6, " "));
        assertEquals("a+++aa", pad("+++", 6, "a"));
        assertEquals("a+++ba", pad("+++", 6, "ab"));
        assertEquals("a+++bc", pad("+++", 6, "abcd"));

        assertEquals("+++---***", pad("+++---***", 6, "abcd"));
    }

    @Test
    void testPadEnd() {
        assertThrows(IllegalArgumentException.class, () -> padEnd("", -1, " "));

        assertEquals("      ", padEnd(null, 6, null));
        assertEquals("      ", padEnd(null, 6, ""));
        assertEquals("      ", padEnd(null, 6, " "));
        assertEquals("aaaaaa", padEnd(null, 6, "a"));
        assertEquals("ababab", padEnd(null, 6, "ab"));
        assertEquals("abcdab", padEnd(null, 6, "abcd"));

        assertEquals("      ", padEnd("", 6, null));
        assertEquals("      ", padEnd("", 6, ""));
        assertEquals("      ", padEnd("", 6, " "));
        assertEquals("aaaaaa", padEnd("", 6, "a"));
        assertEquals("ababab", padEnd("", 6, "ab"));
        assertEquals("abcdab", padEnd("", 6, "abcd"));

        assertEquals("+++   ", padEnd("+++", 6, null));
        assertEquals("+++   ", padEnd("+++", 6, ""));
        assertEquals("+++   ", padEnd("+++", 6, " "));
        assertEquals("+++aaa", padEnd("+++", 6, "a"));
        assertEquals("+++aba", padEnd("+++", 6, "ab"));
        assertEquals("+++abc", padEnd("+++", 6, "abcd"));

        assertEquals("+++---***", padEnd("+++---***", 6, "abcd"));
    }

    @Test
    void testPadStart() {
        assertThrows(IllegalArgumentException.class, () -> padStart("", -1, " "));

        assertEquals("      ", padStart(null, 6, null));
        assertEquals("      ", padStart(null, 6, ""));
        assertEquals("      ", padStart(null, 6, " "));
        assertEquals("aaaaaa", padStart(null, 6, "a"));
        assertEquals("ababab", padStart(null, 6, "ab"));
        assertEquals("abcdab", padStart(null, 6, "abcd"));

        assertEquals("      ", padStart("", 6, null));
        assertEquals("      ", padStart("", 6, ""));
        assertEquals("      ", padStart("", 6, " "));
        assertEquals("aaaaaa", padStart("", 6, "a"));
        assertEquals("ababab", padStart("", 6, "ab"));
        assertEquals("abcdab", padStart("", 6, "abcd"));

        assertEquals("   +++", padStart("+++", 6, null));
        assertEquals("   +++", padStart("+++", 6, ""));
        assertEquals("   +++", padStart("+++", 6, " "));
        assertEquals("aaa+++", padStart("+++", 6, "a"));
        assertEquals("aba+++", padStart("+++", 6, "ab"));
        assertEquals("abc+++", padStart("+++", 6, "abcd"));

        assertEquals("+++---***", padStart("+++---***", 6, "abcd"));
    }

    @Test
    void testSnakeCase() {
        assertNull(snakeCase(null));
        assertEquals("", snakeCase(""));
        assertEquals("foo_bar", snakeCase("fooBar"));
        assertEquals("foo_bar", snakeCase("Foo Bar"));
        assertEquals("foo_bar", snakeCase("--foo-bar--"));
        assertEquals("foo_bar", snakeCase("__foo_Bar__"));
    }


    @Test
    void testTake() {
        for (int i = -100; i < 0; i++) {
            final int n = i;
            assertThrows(IllegalArgumentException.class, () -> take("a", n));
        }

        final String nullString = null;

        assertEquals("", take(nullString, 100));
        assertEquals("", take("100", 0));

        assertEquals("abc", take("abcdefg", 3));
        assertEquals("abc", take("abc", 3));
        assertEquals("abc", take("abc", 100));
    }

    @Test
    void testTakeRight() {
        for (int i = -100; i < 0; i++) {
            final int n = i;
            assertThrows(IllegalArgumentException.class, () -> takeRight("a", n));
        }

        assertEquals("", takeRight(null, 100));
        assertEquals("", takeRight("100", 0));

        assertEquals("efg", takeRight("abcdefg", 3));
        assertEquals("abc", takeRight("abc", 3));
        assertEquals("abc", takeRight("abc", 100));
    }


    @Test
    void replaceAll3() {
        var pattern = Pattern.compile("\\{}");

        assertNull(replaceAll(null, null, null));
        assertEquals("abc", replaceAll("abc", null, null));
        assertEquals("abc", replaceAll("abc", pattern, null));

        assertEquals("a={0} b={1} c={2}", replaceAll("a={} b={} c={}", pattern, i -> "{" + i + "}"));
    }
}
