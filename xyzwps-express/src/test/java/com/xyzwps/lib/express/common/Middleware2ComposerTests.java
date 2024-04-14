package com.xyzwps.lib.express.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.express.common.Middleware2Composer.compose;

class Middleware2ComposerTests {

    @Test
    void composeNothing() {
        var mw = compose();
        var sb1 = new StringBuilder();
        var sb2 = new StringBuilder();
        mw.call(sb1, sb2, Next.EMPTY);

        assertEquals("", sb1.toString());
        assertEquals("", sb2.toString());
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
        assertThrows(NullPointerException.class, () -> compose((Middleware2<StringBuilder, StringBuilder>) null));

        assertThrows(NullPointerException.class, () -> compose(makeMw(""), null));
        assertThrows(NullPointerException.class, () -> compose(null, makeMw("")));

        assertThrows(NullPointerException.class, () -> compose(makeMw("1"), makeMw("2"), null));
        assertThrows(NullPointerException.class, () -> compose(makeMw("1"), null, makeMw("3")));
        assertThrows(NullPointerException.class, () -> compose(null, makeMw("2"), makeMw("3")));
    }

    @Test
    void composerThrowExceptionBeforeNext() {
        var mw = compose(
                makeMw("1st-"),
                (p1, p2, n) -> {
                    var prefix = "2nd-";
                    p1.append(prefix).append("p1b ");
                    p2.append(prefix).append("p2b ");
                    throw new IllegalStateException();
                },
                makeMw("3rd-"),
                makeMw("4th-"));
        var sb1 = new StringBuilder();
        var sb2 = new StringBuilder();

        assertThrows(IllegalStateException.class, () -> mw.call(sb1, sb2, Next.EMPTY));

        assertEquals("1st-p1b 2nd-p1b ", sb1.toString());
        assertEquals("1st-p2b 2nd-p2b ", sb2.toString());
    }

    @Test
    void composerThrowExceptionAfterNext() {
        var mw = compose(
                makeMw("1st-"),
                (p1, p2, n) -> {
                    var prefix = "2nd-";
                    p1.append(prefix).append("p1b ");
                    p2.append(prefix).append("p2b ");
                    n.call();
                    throw new IllegalStateException();
                },
                makeMw("3rd-"),
                makeMw("4th-"));
        var sb1 = new StringBuilder();
        var sb2 = new StringBuilder();

        assertThrows(IllegalStateException.class, () -> mw.call(sb1, sb2, Next.EMPTY));

        assertEquals("1st-p1b 2nd-p1b 3rd-p1b 4th-p1b 4th-p1a 3rd-p1a ", sb1.toString());
        assertEquals("1st-p2b 2nd-p2b 3rd-p2b 4th-p2b 4th-p2a 3rd-p2a ", sb2.toString());
    }

    @Test
    void composerMiddlewareWithoutCallingNext() {
        var mw = compose(
                makeMw("1st-"),
                (p1, p2, n) -> {
                    var prefix = "2nd-";
                    p1.append(prefix).append("p1b ");
                    p2.append(prefix).append("p2b ");
                    p2.append(prefix).append("p2a ");
                    p1.append(prefix).append("p1a ");
                },
                makeMw("3rd-"),
                makeMw("4th-"));
        var sb1 = new StringBuilder();
        var sb2 = new StringBuilder();

        mw.call(sb1, sb2, Next.EMPTY);

        assertEquals("1st-p1b 2nd-p1b 2nd-p1a 1st-p1a ", sb1.toString());
        assertEquals("1st-p2b 2nd-p2b 2nd-p2a 1st-p2a ", sb2.toString());
    }
}
