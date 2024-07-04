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
                var path = UrlPath.of("");
                assertEquals(0, path.length());
                assertEquals("/", path.rawPath());
                assertFalse(path.hasStar2());
            }
            {
                var path = UrlPath.of("/");
                assertEquals(0, path.length());
                assertEquals("/", path.rawPath());
                assertFalse(path.hasStar2());
            }
            {
                var path = UrlPath.of("/////");
                assertEquals(0, path.length());
                assertEquals("/", path.rawPath());
                assertFalse(path.hasStar2());
            }
        }

        @Test
        void parseNull() {
            var path = UrlPath.of(null);
            assertEquals(0, path.length());
            assertEquals("/", path.rawPath());
            assertFalse(path.hasStar2());
        }

        @Test
        void parseSimple() {
            var path = UrlPath.of("/a/b/c");
            assertEquals(3, path.length());
            assertEquals(new UrlSegment.Text("a"), path.get(0));
            assertEquals(new UrlSegment.Text("b"), path.get(1));
            assertEquals(new UrlSegment.Text("c"), path.get(2));
            assertEquals("/a/b/c", path.rawPath());
            assertFalse(path.hasStar2());
        }

        @Test
        void parseStar() {
            var path = UrlPath.of("/a/*/c");
            assertEquals(3, path.length());
            assertEquals(new UrlSegment.Text("a"), path.get(0));
            assertEquals(UrlSegment.Star.INSTANCE, path.get(1));
            assertEquals(new UrlSegment.Text("c"), path.get(2));
            assertEquals("/a/*/c", path.rawPath());
            assertFalse(path.hasStar2());
        }

        @Test
        void parseStar2() {
            assertThrows(IllegalArgumentException.class, () -> UrlPath.of("/a/**/c"));
            assertThrows(IllegalArgumentException.class, () -> UrlPath.of("/**/b/c"));

            var path = UrlPath.of("/a/b/**");
            assertEquals(3, path.length());
            assertEquals(new UrlSegment.Text("a"), path.get(0));
            assertEquals(new UrlSegment.Text("b"), path.get(1));
            assertEquals(UrlSegment.Star2.INSTANCE, path.get(2));
            assertEquals("/a/b/**", path.rawPath());
            assertTrue(path.hasStar2());
        }

        @Test
        void parseParam() {
            var path = UrlPath.of("/a/:b/c");
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
            var path = UrlPath.of("/a/b/c");

            if (path.match(UrlPath.of("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/1/a/b/c"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }


            if (path.match(UrlPath.of("/2/1/a/b/c"), 2) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/3/2/1/a/b/c"), 3) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/a/a/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/c"), 0));
        }

        @Test
        void matchStar() {
            var path = UrlPath.of("/a/*/c");

            if (path.match(UrlPath.of("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/a/d/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/a/a/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/1/a/b/c"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/2/1/a/e/c"), 2) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/3/2/1/a/f/c"), 3) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/c"), 0));
        }

        @Test
        void matchParam1() {
            var path = UrlPath.of("/a/:name/c");

            if (path.match(UrlPath.of("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/a/d/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "d")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/a/a/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "a")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/1/a/b/c"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/2/1/a/e/c"), 2) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "e")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/3/2/1/a/f/c"), 3) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "f")), matched.pathVariables());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/c"), 0));
        }

        @Test
        void matchParam2() {
            var path = UrlPath.of("/a/:name/:id");

            if (path.match(UrlPath.of("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b"), Pair.of("id", "c")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/a/d/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "d"), Pair.of("id", "c")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/a/a/a"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "a"), Pair.of("id", "a")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/1/a/b/g"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b"), Pair.of("id", "g")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/2/1/a/e/c"), 2) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "e"), Pair.of("id", "c")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/3/2/1/a/f/c"), 3) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "f"), Pair.of("id", "c")), matched.pathVariables());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/c"), 0));
        }

        @Test
        void matchStar2() {
            var path = UrlPath.of("/a/**");

            if (path.match(UrlPath.of("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/a/b"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/a"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/x/a"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/b/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.match(UrlPath.of("/aa"), 0));
        }

    }

    @Nested
    class PrefixOfTests {

        @Test
        void prefixOfSimple() {
            var path = UrlPath.of("/a/b/c");

            if (path.prefixOf(UrlPath.of("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.of("/a/b/c/d"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.of("/1/a/b/c"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/a/b/d"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/a/b/c"), 1));
        }

        @Test
        void prefixOfStar() {
            var path = UrlPath.of("/a/*/c");

            if (path.prefixOf(UrlPath.of("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.of("/a/c/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.of("/a/d/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.of("/a/b/c/d"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.of("/1/a/b/c"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertTrue(matched.pathVariables().isEmpty());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/a/b/c"), 1));
        }

        @Test
        void prefixOfParam() {
            var path = UrlPath.of("/a/:name/c");

            if (path.prefixOf(UrlPath.of("/a/b/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.of("/a/b/c/d"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.of("/a/d/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "d")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.of("/a/a/c"), 0) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "a")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.of("/1/a/b/c"), 1) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "b")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.prefixOf(UrlPath.of("/2/1/a/e/c"), 2) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "e")), matched.pathVariables());
            } else {
                fail();
            }

            if (path.match(UrlPath.of("/3/2/1/a/f/c"), 3) instanceof UrlPath.MatchResult.Matched matched) {
                assertIterableEquals(List.of(Pair.of("name", "f")), matched.pathVariables());
            } else {
                fail();
            }

            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/b/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/a/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/b/c"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/a"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/b"), 0));
            assertInstanceOf(UrlPath.MatchResult.NotMatched.class, path.prefixOf(UrlPath.of("/c"), 0));
        }
    }
}
