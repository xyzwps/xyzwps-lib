package com.xyzwps.lib.express.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MiddlewareTests {

    @Test
    void compose3() {
        var sb = new StringBuilder();
        var mw = Middleware.<DemoContext, DemoMiddleware>compose(
                (m1, m2) -> (ctx) -> m1.call(new DemoContext(m2, ctx)),
                List.of(
                        (ctx) -> {
                            sb.append(" 1-1 ");
                            ctx.next();
                            sb.append(" 1-2 ");
                        },
                        (ctx) -> {
                            sb.append(" 2-1 ");
                            ctx.next();
                            sb.append(" 2-2 ");
                        },
                        (ctx) -> {
                            sb.append(" 3-1 ");
                            ctx.next();
                            sb.append(" 3-2 ");
                        }
                )
        );
        mw.call(new DemoContext());
        assertEquals(" 1-1  2-1  3-1  3-2  2-2  1-2 ", sb.toString());
    }

    @Test
    void compose2() {
        var sb = new StringBuilder();
        var mw = Middleware.<DemoContext, DemoMiddleware>compose(
                (m1, m2) -> (ctx) -> m1.call(new DemoContext(m2, ctx)),
                List.of(
                        (ctx) -> {
                            sb.append(" 1-1 ");
                            ctx.next();
                            sb.append(" 1-2 ");
                        },
                        (ctx) -> {
                            sb.append(" 2-1 ");
                            ctx.next();
                            sb.append(" 2-2 ");
                        }
                )
        );
        mw.call(new DemoContext());
        assertEquals(" 1-1  2-1  2-2  1-2 ", sb.toString());
    }

    @Test
    void compose1() {
        var sb = new StringBuilder();
        var mw = Middleware.<DemoContext, DemoMiddleware>compose(
                (m1, m2) -> (ctx) -> m1.call(new DemoContext(m2, ctx)),
                List.of(
                        (ctx) -> {
                            sb.append(" 1-1 ");
                            ctx.next();
                            sb.append(" 1-2 ");
                        }
                )
        );
        mw.call(new DemoContext());
        assertEquals(" 1-1  1-2 ", sb.toString());
    }

    @Test
    void compose0() {
        assertThrows(IllegalArgumentException.class, () -> Middleware.<DemoContext, DemoMiddleware>compose(
                (m1, m2) -> (ctx) -> m1.call(new DemoContext(m2, ctx)),
                List.of()
        ));

        assertThrows(IllegalArgumentException.class, () -> Middleware.<DemoContext, DemoMiddleware>compose(
                (m1, m2) -> (ctx) -> m1.call(new DemoContext(m2, ctx)),
                null
        ));

        assertThrows(IllegalArgumentException.class, () -> Middleware.<DemoContext, DemoMiddleware>compose(
                null,
                List.of()
        ));

        assertEquals("ms[0] cannot be null",
                assertThrows(IllegalArgumentException.class, () -> Middleware.<DemoContext, DemoMiddleware>compose(
                        (m1, m2) -> (ctx) -> m1.call(new DemoContext(m2, ctx)),
                        Arrays.asList(null, DemoContext::next)
                )).getMessage());

        assertEquals("ms[1] cannot be null",
                assertThrows(IllegalArgumentException.class, () -> Middleware.<DemoContext, DemoMiddleware>compose(
                        (m1, m2) -> (ctx) -> m1.call(new DemoContext(m2, ctx)),
                        Arrays.asList(DemoContext::next, null)
                )).getMessage());
    }


    interface DemoMiddleware extends Middleware<DemoContext> {
    }

    static class DemoContext implements Middleware.Context<DemoContext> {

        private final DemoMiddleware mw;
        private final DemoContext prev;

        DemoContext(DemoMiddleware mw, DemoContext prev) {
            this.mw = mw;
            this.prev = prev;
        }

        DemoContext() {
            this(null, null);
        }

        @Override
        public void next() {
            if (mw != null) mw.call(prev);
        }
    }
}
