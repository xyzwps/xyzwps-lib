package com.xyzwps.lib.express.middleware.router;

import com.xyzwps.lib.dollar.Pair;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.express.middleware.router.HPath.*;
import static com.xyzwps.lib.dollar.Dollar.$;

/**
 * <pre>
 *     1.  /
 *         /abc
 *         /abc/defg
 *     2.  /*
 *         /abc/*
 *         /abc/*<span>/</span>defg
 *         /*<span>/</span>def/*<span>/</span>defg
 *         /abc/*<span>/</span>*<span>/</span>defg
 *     3.  /**
 *         /abc/**
 *         /abc/defg/**
 *     4.  /{id}
 *         /abc/{id}
 *         /abc/{id}<span>/</span>defg
 *         /*<span>/</span>def/{id}<span>/</span>defg
 *         /abc/{id}<span>/</span>{id}<span>/</span>defg
 *         /abc/{id}<span>/</span>{id2}<span>/</span>defg
 * </pre>
 */
class HPathTests {

    static void fromEquals(String target, List<String> candidates) {
        for (var c : candidates) {
            var str = c;
            assertEquals(target, HPath.from(str).toString(), target + " does not equals to " + str);

            str = Arrays.stream(c.split("/"))
                    .filter($::isNotEmpty)
                    .collect(Collectors.joining("//"));
            assertEquals(target, HPath.from(str).toString(), target + " does not equals to " + str);

            str = Arrays.stream(c.split("/"))
                    .filter($::isNotEmpty)
                    .collect(Collectors.joining("///"));
            assertEquals(target, HPath.from(str).toString(), target + " does not equals to " + str);

            str = "///" + c + "///";
            assertEquals(target, HPath.from(str).toString(), target + " does not equals to " + str);
        }
    }

    static void testFromEquals(String path) {
        fromEquals("/" + path, List.of("/" + path, path + "/", path, "/" + path + "/"));
    }

    @Test
    void testFrom() {
        // 1
        testFromEquals("");
        testFromEquals("abc");
        testFromEquals("abc/defg");

        // 2
        testFromEquals("*");
        testFromEquals("abc/*");
        testFromEquals("abc/*/defg");
        testFromEquals("*/def/*/defg");
        testFromEquals("abc/*/*/defg");

        // 3
        testFromEquals("**");
        testFromEquals("abc/**");
        testFromEquals("abc/defg/**");
        assertThrows(IllegalArgumentException.class, () -> HPath.from("/**/a"));
        assertThrows(IllegalArgumentException.class, () -> HPath.from("/**/**"));
        assertThrows(IllegalArgumentException.class, () -> HPath.from("/a/**/a"));

        // 4
        testFromEquals("{id}");
        testFromEquals("abc/{id}");
        testFromEquals("abc/{id}/defg");
        testFromEquals("*/def/{id}/defg");
        testFromEquals("abc/{id}/{id}/defg");
        testFromEquals("abc/{id}/{id2}/defg");
    }

    @Test
    void testMatch() {

        // 1
        assertTrue(from("/").match(new String[0], 0));
        assertFalse(from("/").match(new String[0], 1));
        assertFalse(from("/").match(new String[0], 2));
        assertFalse(from("/").match(new String[0], 3));
        assertFalse(from("/").match(new String[]{"a"}, 0));
        assertFalse(from("/").match(new String[]{"a", "b"}, 0));

        assertTrue(from("/abc").match(new String[]{"abc"}, 0));
        assertTrue(from("/abc").match(new String[]{"ddd", "abc"}, 1));
        assertTrue(from("/abc").match(new String[]{"ddd", "eee", "abc"}, 2));
        assertFalse(from("/abc").match(new String[0], 1));
        assertFalse(from("/abc").match(new String[]{"abc"}, 1));
        assertFalse(from("/abc").match(new String[]{"abc"}, 2));
        assertFalse(from("/abc").match(new String[]{"abc"}, 3));
        assertFalse(from("/abc").match(new String[]{"ab"}, 0));
        assertFalse(from("/abc").match(new String[]{"abcd"}, 0));
        assertFalse(from("/abc").match(new String[]{"abc", "def"}, 0));
        assertFalse(from("/abc").match(new String[]{"abc"}, 1));

        assertTrue(from("/abc/defg").match(new String[]{"abc", "defg"}, 0));
        assertTrue(from("/abc/defg").match(new String[]{"abc", "abc", "defg"}, 1));
        assertFalse(from("/abc/defg").match(new String[0], 0));
        assertFalse(from("/abc/defg").match(new String[]{"abc"}, 0));
        assertFalse(from("/abc/defg").match(new String[]{"defg"}, 0));
        assertFalse(from("/abc/defg").match(new String[]{"abc", "def"}, 0));
        assertFalse(from("/abc/defg").match(new String[]{"bc", "defg"}, 0));
        assertFalse(from("/abc/defg").match(new String[]{"abc", "defg", "hij"}, 0));
        assertFalse(from("/abc/defg").match(new String[]{"sss", "abc", "defg"}, 0));
        assertFalse(from("/abc/defg").match(new String[]{"abc", "dd", "defg"}, 0));


        // 2
        assertTrue(from("/*").match(new String[]{"abc"}, 0));
        assertTrue(from("/*").match(new String[]{"def"}, 0));
        assertTrue(from("/*").match(new String[]{"dfd", "abc"}, 1));
        assertTrue(from("/*").match(new String[]{"fas", "def"}, 1));
        assertFalse(from("/*").match(new String[0], 0));
        assertFalse(from("/*").match(new String[]{"abc", "def"}, 0));

        assertTrue(from("/abc/*").match(new String[]{"abc", "def"}, 0));
        assertTrue(from("/abc/*").match(new String[]{"abc", "abc"}, 0));
        assertTrue(from("/abc/*").match(new String[]{"abcx", "abc", "def"}, 1));

        assertTrue(from("/abc/*/defg").match(new String[]{"abc", "xxx", "defg"}, 0));

        assertTrue(from("/*/def/*/defg").match(new String[]{"abc", "def", "xxx", "defg"}, 0));

        assertTrue(from("/abc/*/*/defg").match(new String[]{"abc", "def", "xxx", "defg"}, 0));

        // 3
        assertTrue(from("/**").match(new String[0], 0));
        assertTrue(from("/**").match(new String[]{"abc"}, 0));
        assertTrue(from("/**").match(new String[]{"abc", "def"}, 0));
        assertTrue(from("/**").match(new String[]{"abc", "def", "hij"}, 0));
        assertTrue(from("/**").match(new String[]{"abc"}, 1));
        assertTrue(from("/**").match(new String[]{"abc", "def"}, 1));
        assertTrue(from("/**").match(new String[]{"abc", "def", "hij"}, 1));
        assertTrue(from("/**").match(new String[]{"abc", "def"}, 2));
        assertTrue(from("/**").match(new String[]{"abc", "def", "hij"}, 2));
        assertTrue(from("/**").match(new String[]{"abc", "def", "hij"}, 3));

        assertTrue(from("/abc/**").match(new String[]{"abc", "def", "hij"}, 0));

        assertTrue(from("/abc/defg/**").match(new String[]{"abc", "defg", "hij"}, 0));


        // 4
        assertTrue(from("/{id}").match(new String[]{"abc"}, 0));

        assertTrue(from("/abc/{id}").match(new String[]{"abc", "def"}, 0));

        assertTrue(from("/abc/{id}/defg").match(new String[]{"abc", "def", "defg"}, 0));

        assertTrue(from("/*/abc/{id}/defg").match(new String[]{"ddd", "abc", "def", "defg"}, 0));

        assertTrue(from("abc/{id}/{id}/defg").match(new String[]{"abc", "ddd", "def", "defg"}, 0));

        assertTrue(from("abc/{id}/{id2}/defg").match(new String[]{"abc", "ddd", "def", "defg"}, 0));
    }

    @Test
    void testPathVars() {
        assertIterableEquals(
                List.of(),
                HPath.from("/**").pathVars(new String[0], 0)
        );

        assertIterableEquals(
                List.of(),
                HPath.from("/abc/*").pathVars(new String[]{"abc", "def"}, 0)
        );

        assertIterableEquals(
                List.of(Pair.of("id", "abc")),
                HPath.from("/{id}").pathVars(new String[]{"abc"}, 0)
        );

        assertIterableEquals(
                List.of(Pair.of("id", "abc"), Pair.of("id2", "def")),
                HPath.from("/{id}/{id2}").pathVars(new String[]{"abc", "def"}, 0)
        );

        assertIterableEquals(
                List.of(Pair.of("id", "def"), Pair.of("id2", "hij")),
                HPath.from("/{id}/{id2}").pathVars(new String[]{"abc", "def", "hij"}, 1)
        );

        assertIterableEquals(
                List.of(Pair.of("id", "abc"), Pair.of("id", "def")),
                HPath.from("/{id}/{id}").pathVars(new String[]{"abc", "def"}, 0)
        );
    }
}
