package com.xyzwps.lib.express.common;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.express.common.Middleware2Composer.compose;

class Middleware2ComposerTests {

    static final Class<IllegalArgumentException> IAE = IllegalArgumentException.class;
    static final Class<NullPointerException> NPE = NullPointerException.class;

    @Nested
    class VarargsComposer {

        @Test
        void composeNothing() {
            assertEquals("No middlewares to compose", assertThrows(IAE, Middleware2Composer::compose).getMessage());
        }

        static Middleware2<StringBuilder, StringBuilder> makeMw(String prefix) {
            return (p1, p2, n) -> {
                p1.append(prefix).append("p1b ");
                p2.append(prefix).append("p2b ");
                n.call();
                p2.append(prefix).append("p2a ");
                p1.append(prefix).append("p1a ");
            };
        }

        @Test
        void composeOne() {
            var mw = compose(makeMw(""));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            mw.call(sb1, sb2, Next.EMPTY);

            assertEquals("p1b p1a ", sb1.toString());
            assertEquals("p2b p2a ", sb2.toString());
        }

        @Test
        void composeTwo() {
            var mw = compose(makeMw("1st-"), makeMw("2nd-"));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            mw.call(sb1, sb2, Next.EMPTY);

            assertEquals("1st-p1b 2nd-p1b 2nd-p1a 1st-p1a ", sb1.toString());
            assertEquals("1st-p2b 2nd-p2b 2nd-p2a 1st-p2a ", sb2.toString());
        }

        @Test
        void composeThree() {
            var mw = compose(makeMw("1st-"), makeMw("2nd-"), makeMw("3rd-"));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            mw.call(sb1, sb2, Next.EMPTY);

            assertEquals("1st-p1b 2nd-p1b 3rd-p1b 3rd-p1a 2nd-p1a 1st-p1a ", sb1.toString());
            assertEquals("1st-p2b 2nd-p2b 3rd-p2b 3rd-p2a 2nd-p2a 1st-p2a ", sb2.toString());
        }

        @Test
        void composeFour() {
            var mw = compose(makeMw("1st-"), makeMw("2nd-"), makeMw("3rd-"), makeMw("4th-"));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            mw.call(sb1, sb2, Next.EMPTY);

            assertEquals("1st-p1b 2nd-p1b 3rd-p1b 4th-p1b 4th-p1a 3rd-p1a 2nd-p1a 1st-p1a ", sb1.toString());
            assertEquals("1st-p2b 2nd-p2b 3rd-p2b 4th-p2b 4th-p2a 3rd-p2a 2nd-p2a 1st-p2a ", sb2.toString());
        }

        @Test
        void composeNull() {
            assertThrows(NPE, () -> compose((Middleware2<StringBuilder, StringBuilder>) null));

            assertThrows(NPE, () -> compose(makeMw(""), null));
            assertThrows(NPE, () -> compose(null, makeMw("")));

            assertThrows(NPE, () -> compose(makeMw("1"), makeMw("2"), null));
            assertThrows(NPE, () -> compose(makeMw("1"), null, makeMw("3")));
            assertThrows(NPE, () -> compose(null, makeMw("2"), makeMw("3")));
        }

        @Test
        void composerThrowExceptionBeforeNext() {
            var mw = compose(makeMw("1st-"), (p1, p2, n) -> {
                var prefix = "2nd-";
                p1.append(prefix).append("p1b ");
                p2.append(prefix).append("p2b ");
                throw new IllegalStateException();
            }, makeMw("3rd-"), makeMw("4th-"));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            assertThrows(IllegalStateException.class, () -> mw.call(sb1, sb2, Next.EMPTY));

            assertEquals("1st-p1b 2nd-p1b ", sb1.toString());
            assertEquals("1st-p2b 2nd-p2b ", sb2.toString());
        }

        @Test
        void composerThrowExceptionAfterNext() {
            var mw = compose(makeMw("1st-"), (p1, p2, n) -> {
                var prefix = "2nd-";
                p1.append(prefix).append("p1b ");
                p2.append(prefix).append("p2b ");
                n.call();
                throw new IllegalStateException();
            }, makeMw("3rd-"), makeMw("4th-"));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            assertThrows(IllegalStateException.class, () -> mw.call(sb1, sb2, Next.EMPTY));

            assertEquals("1st-p1b 2nd-p1b 3rd-p1b 4th-p1b 4th-p1a 3rd-p1a ", sb1.toString());
            assertEquals("1st-p2b 2nd-p2b 3rd-p2b 4th-p2b 4th-p2a 3rd-p2a ", sb2.toString());
        }

        @Test
        void composerMiddlewareWithoutCallingNext() {
            var mw = compose(makeMw("1st-"), (p1, p2, n) -> {
                var prefix = "2nd-";
                p1.append(prefix).append("p1b ");
                p2.append(prefix).append("p2b ");
                p2.append(prefix).append("p2a ");
                p1.append(prefix).append("p1a ");
            }, makeMw("3rd-"), makeMw("4th-"));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            mw.call(sb1, sb2, Next.EMPTY);

            assertEquals("1st-p1b 2nd-p1b 2nd-p1a 1st-p1a ", sb1.toString());
            assertEquals("1st-p2b 2nd-p2b 2nd-p2a 1st-p2a ", sb2.toString());
        }

    }

    @Nested
    class ListComposer {

        @Test
        void composeNothing() {
            assertEquals("No middlewares to compose", assertThrows(IAE, () -> compose(List.of())).getMessage());
            assertEquals("No middlewares to compose", assertThrows(IAE, () -> compose((List<Middleware2<String, String>>) null)).getMessage());
        }

        static Middleware2<StringBuilder, StringBuilder> makeMw(String prefix) {
            return (p1, p2, n) -> {
                p1.append(prefix).append("p1b ");
                p2.append(prefix).append("p2b ");
                n.call();
                p2.append(prefix).append("p2a ");
                p1.append(prefix).append("p1a ");
            };
        }

        @Test
        void composeOne() {
            var mw = compose(List.of(makeMw("")));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            mw.call(sb1, sb2, Next.EMPTY);

            assertEquals("p1b p1a ", sb1.toString());
            assertEquals("p2b p2a ", sb2.toString());
        }

        @Test
        void composeTwo() {
            var mw = compose(List.of(makeMw("1st-"), makeMw("2nd-")));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            mw.call(sb1, sb2, Next.EMPTY);

            assertEquals("1st-p1b 2nd-p1b 2nd-p1a 1st-p1a ", sb1.toString());
            assertEquals("1st-p2b 2nd-p2b 2nd-p2a 1st-p2a ", sb2.toString());
        }

        @Test
        void composeThree() {
            var mw = compose(List.of(makeMw("1st-"), makeMw("2nd-"), makeMw("3rd-")));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            mw.call(sb1, sb2, Next.EMPTY);

            assertEquals("1st-p1b 2nd-p1b 3rd-p1b 3rd-p1a 2nd-p1a 1st-p1a ", sb1.toString());
            assertEquals("1st-p2b 2nd-p2b 3rd-p2b 3rd-p2a 2nd-p2a 1st-p2a ", sb2.toString());
        }

        @Test
        void composeFour() {
            var mw = compose(List.of(makeMw("1st-"), makeMw("2nd-"), makeMw("3rd-"), makeMw("4th-")));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            mw.call(sb1, sb2, Next.EMPTY);

            assertEquals("1st-p1b 2nd-p1b 3rd-p1b 4th-p1b 4th-p1a 3rd-p1a 2nd-p1a 1st-p1a ", sb1.toString());
            assertEquals("1st-p2b 2nd-p2b 3rd-p2b 4th-p2b 4th-p2a 3rd-p2a 2nd-p2a 1st-p2a ", sb2.toString());
        }

        @Test
        void composeNull() {
            assertThrows(NPE, () -> compose(Collections.singletonList((Middleware2<StringBuilder, StringBuilder>) null)));

            assertThrows(NPE, () -> compose(Arrays.asList(makeMw(""), null)));
            assertThrows(NPE, () -> compose(Arrays.asList(null, makeMw(""))));

            assertThrows(NPE, () -> compose(Arrays.asList(makeMw("1"), makeMw("2"), null)));
            assertThrows(NPE, () -> compose(Arrays.asList(makeMw("1"), null, makeMw("3"))));
            assertThrows(NPE, () -> compose(Arrays.asList(null, makeMw("2"), makeMw("3"))));
        }

        @Test
        void composerThrowExceptionBeforeNext() {
            var mw = compose(List.of(makeMw("1st-"), (p1, p2, n) -> {
                var prefix = "2nd-";
                p1.append(prefix).append("p1b ");
                p2.append(prefix).append("p2b ");
                throw new IllegalStateException();
            }, makeMw("3rd-"), makeMw("4th-")));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            assertThrows(IllegalStateException.class, () -> mw.call(sb1, sb2, Next.EMPTY));

            assertEquals("1st-p1b 2nd-p1b ", sb1.toString());
            assertEquals("1st-p2b 2nd-p2b ", sb2.toString());
        }

        @Test
        void composerThrowExceptionAfterNext() {
            var mw = compose(List.of(makeMw("1st-"), (p1, p2, n) -> {
                var prefix = "2nd-";
                p1.append(prefix).append("p1b ");
                p2.append(prefix).append("p2b ");
                n.call();
                throw new IllegalStateException();
            }, makeMw("3rd-"), makeMw("4th-")));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            assertThrows(IllegalStateException.class, () -> mw.call(sb1, sb2, Next.EMPTY));

            assertEquals("1st-p1b 2nd-p1b 3rd-p1b 4th-p1b 4th-p1a 3rd-p1a ", sb1.toString());
            assertEquals("1st-p2b 2nd-p2b 3rd-p2b 4th-p2b 4th-p2a 3rd-p2a ", sb2.toString());
        }

        @Test
        void composerMiddlewareWithoutCallingNext() {
            var mw = compose(List.of(makeMw("1st-"), (p1, p2, n) -> {
                var prefix = "2nd-";
                p1.append(prefix).append("p1b ");
                p2.append(prefix).append("p2b ");
                p2.append(prefix).append("p2a ");
                p1.append(prefix).append("p1a ");
            }, makeMw("3rd-"), makeMw("4th-")));
            var sb1 = new StringBuilder();
            var sb2 = new StringBuilder();

            mw.call(sb1, sb2, Next.EMPTY);

            assertEquals("1st-p1b 2nd-p1b 2nd-p1a 1st-p1a ", sb1.toString());
            assertEquals("1st-p2b 2nd-p2b 2nd-p2a 1st-p2a ", sb2.toString());
        }

    }

}
