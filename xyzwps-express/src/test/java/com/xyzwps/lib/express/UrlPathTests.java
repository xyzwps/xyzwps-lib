package com.xyzwps.lib.express;

import com.xyzwps.lib.dollar.Pair;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UrlPathTests {

    @Nested
    class ParseTests {

        @Test
        void parseEmpty() {
            {
                var path = UrlPath.parse("");
                assertEquals(0, path.length());
                assertEquals("/", path.rawPath());
                assertFalse(path.hasStar2());
            }
            {
                var path = UrlPath.parse("/");
                assertEquals(0, path.length());
                assertEquals("/", path.rawPath());
                assertFalse(path.hasStar2());
            }
            {
                var path = UrlPath.parse("/////");
                assertEquals(0, path.length());
                assertEquals("/", path.rawPath());
                assertFalse(path.hasStar2());
            }
        }

        @Test
        void parseNull() {
            var path = UrlPath.parse(null);
            assertEquals(0, path.length());
            assertEquals("/", path.rawPath());
            assertFalse(path.hasStar2());
        }

        @Test
        void parseSimple() {
            var path = UrlPath.parse("/a/b/c");
            assertEquals(3, path.length());
            assertEquals(new UrlSegment.Text("a"), path.get(0));
            assertEquals(new UrlSegment.Text("b"), path.get(1));
            assertEquals(new UrlSegment.Text("c"), path.get(2));
            assertEquals("/a/b/c", path.rawPath());
            assertFalse(path.hasStar2());
        }

        @Test
        void parseStar() {
            var path = UrlPath.parse("/a/*/c");
            assertEquals(3, path.length());
            assertEquals(new UrlSegment.Text("a"), path.get(0));
            assertEquals(UrlSegment.Star.INSTANCE, path.get(1));
            assertEquals(new UrlSegment.Text("c"), path.get(2));
            assertEquals("/a/*/c", path.rawPath());
            assertFalse(path.hasStar2());
        }

        @Test
        void parseStar2() {
            assertThrows(IllegalArgumentException.class, () -> UrlPath.parse("/a/**/c"));
            assertThrows(IllegalArgumentException.class, () -> UrlPath.parse("/**/b/c"));

            var path = UrlPath.parse("/a/b/**");
            assertEquals(3, path.length());
            assertEquals(new UrlSegment.Text("a"), path.get(0));
            assertEquals(new UrlSegment.Text("b"), path.get(1));
            assertEquals(UrlSegment.Star2.INSTANCE, path.get(2));
            assertEquals("/a/b/**", path.rawPath());
            assertTrue(path.hasStar2());
        }

        @Test
        void parseParam() {
            var path = UrlPath.parse("/a/:b/c");
            assertEquals(3, path.length());
            assertEquals(new UrlSegment.Text("a"), path.get(0));
            assertEquals(new UrlSegment.Param("b"), path.get(1));
            assertEquals(new UrlSegment.Text("c"), path.get(2));
            assertEquals("/a/:b/c", path.rawPath());
            assertFalse(path.hasStar2());
        }
    }

    @Nested
    class MatchTests {

        @Test
        void matchSimple() {
            var path = UrlPath.parse("/a/b/c");

            if (path.match(UrlPath.parse("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/1/a/b/c"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }


            if (path.match(UrlPath.parse("/2/1/a/b/c"), 2) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/3/2/1/a/b/c"), 3) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/a/a/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/c"), 0));
        }

        @Test
        void matchStar() {
            var path = UrlPath.parse("/a/*/c");

            if (path.match(UrlPath.parse("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/a/d/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/a/a/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/1/a/b/c"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/2/1/a/e/c"), 2) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/3/2/1/a/f/c"), 3) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/c"), 0));
        }

        @Test
        void matchParam1() {
            var path = UrlPath.parse("/a/:name/c");

            if (path.match(UrlPath.parse("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/a/d/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "d")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/a/a/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "a")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/1/a/b/c"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/2/1/a/e/c"), 2) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "e")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/3/2/1/a/f/c"), 3) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "f")), matched.pathVariables());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/c"), 0));
        }

        @Test
        void matchParam2() {
            var path = UrlPath.parse("/a/:name/:id");

            if (path.match(UrlPath.parse("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b"), Pair.of("id", "c")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/a/d/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "d"), Pair.of("id", "c")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/a/a/a"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "a"), Pair.of("id", "a")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/1/a/b/g"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b"), Pair.of("id", "g")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/2/1/a/e/c"), 2) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "e"), Pair.of("id", "c")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/3/2/1/a/f/c"), 3) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "f"), Pair.of("id", "c")), matched.pathVariables());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/c"), 0));
        }

        @Test
        void matchStar2() {
            var path = UrlPath.parse("/a/**");

            if (path.match(UrlPath.parse("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/a/b"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/a"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/x/a"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/b/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.parse("/aa"), 0));
        }

    }

    @Nested
    class PrefixOfTests {

        @Test
        void prefixOfSimple() {
            var path = UrlPath.parse("/a/b/c");

            if (path.prefixOf(UrlPath.parse("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.parse("/a/b/c/d"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.parse("/1/a/b/c"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/a/b/d"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/a/b/c"), 1));
        }

        @Test
        void prefixOfStar() {
            var path = UrlPath.parse("/a/*/c");

            if (path.prefixOf(UrlPath.parse("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.parse("/a/c/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.parse("/a/d/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.parse("/a/b/c/d"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.parse("/1/a/b/c"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/a/b/c"), 1));
        }

        @Test
        void prefixOfParam() {
            var path = UrlPath.parse("/a/:name/c");

            if (path.prefixOf(UrlPath.parse("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.parse("/a/b/c/d"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.parse("/a/d/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "d")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.parse("/a/a/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "a")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.parse("/1/a/b/c"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.parse("/2/1/a/e/c"), 2) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "e")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.parse("/3/2/1/a/f/c"), 3) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "f")), matched.pathVariables());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/b/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.parse("/c"), 0));
        }
    }
}
