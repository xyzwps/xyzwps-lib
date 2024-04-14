package com.xyzwps.lib.express.common;

import java.util.Objects;

public final class ComposedMiddleware2<P1, P2> implements Middleware2<P1, P2> {
    private final Middleware2<P1, P2> composed;

    @SafeVarargs
    public ComposedMiddleware2(Middleware2<P1, P2>... mws) {
        this.composed = compose(mws);
    }


    @Override
    public void call(P1 p1, P2 p2, Next next) {
        composed.call(p1, p2, next);
    }

    @SafeVarargs
    private Middleware2<P1, P2> compose(Middleware2<P1, P2>... mws) {
        if (mws.length == 0) {
            return (p1, p2, n) -> {
            };
        }

        if (mws.length == 1) {
            return Objects.requireNonNull(mws[0], "Middleware cannot be null");
        }

        Middleware2<P1, P2> result = Objects.requireNonNull(mws[mws.length - 1], "Middleware cannot be null");
        for (int i = mws.length - 2; i >= 0; i--) {
            var mw = Objects.requireNonNull(mws[i], "Middleware cannot be null");
            result = compose2(mw, result);
        }

        return result;
    }

    private Middleware2<P1, P2> compose2(Middleware2<P1, P2> mw1, Middleware2<P1, P2> mw2) {
        return (p1, p2, n) -> mw1.call(p1, p2, () -> mw2.call(p1, p2, n));
    }
}
