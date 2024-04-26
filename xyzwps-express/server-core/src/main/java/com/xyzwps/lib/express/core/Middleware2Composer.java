package com.xyzwps.lib.express.core;

import java.util.List;
import java.util.Objects;

import static com.xyzwps.lib.dollar.Dollar.*;

public final class Middleware2Composer {

    @SafeVarargs
    public static <P1, P2> Middleware2<P1, P2> compose(Middleware2<P1, P2>... mws) {
        if (mws.length == 0) {
            throw new IllegalArgumentException("No middlewares to compose");
        }

        if (mws.length == 1) {
            return Objects.requireNonNull(mws[0], "Middleware cannot be null");
        }

        Middleware2<P1, P2> result = Objects.requireNonNull(mws[mws.length - 1], "Middleware cannot be null");
        for (int i = mws.length - 2; i >= 0; i--) {
            var mw = mws[i];
            result = compose2(mw, result);
        }

        return result;
    }

    public static <P1, P2> Middleware2<P1, P2> compose(List<Middleware2<P1, P2>> mws) {
        if ($.isEmpty(mws)) {
            throw new IllegalArgumentException("No middlewares to compose");
        }

        if (mws.size() == 1) {
            return Objects.requireNonNull(mws.getFirst(), "Middleware cannot be null");
        }

        Middleware2<P1, P2> result = Objects.requireNonNull(mws.getLast(), "Middleware cannot be null");
        for (int i = mws.size() - 2; i >= 0; i--) {
            var mw = mws.get(i);
            result = compose2(mw, result);
        }

        return result;
    }

    /**
     * @throws NullPointerException if any of two argument is null
     */
    public static <P1, P2> Middleware2<P1, P2> compose2(Middleware2<P1, P2> mw1, Middleware2<P1, P2> mw2) {
        Objects.requireNonNull(mw1);
        Objects.requireNonNull(mw2);
        return (p1, p2, n) -> mw1.call(p1, p2, () -> mw2.call(p1, p2, n));
    }
}
