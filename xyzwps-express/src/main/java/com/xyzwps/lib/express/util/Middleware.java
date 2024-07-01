package com.xyzwps.lib.express.util;

import com.xyzwps.lib.bedrock.Args;

import java.util.List;
import java.util.function.BiFunction;

public interface Middleware<C extends Middleware.Context<C>> {

    /**
     * @param context cannot be null
     */
    void call(C context);

    interface Context<C> {
        void next();
    }


    static <C extends Middleware.Context<C>, M extends Middleware<C>> M compose(
            BiFunction<M, M, M> wrap,
            M[] ms
    ) {
        Args.notNull(wrap, "wrap cannot be null");
        Args.notEmpty(ms, "ms cannot be empty");
        Args.itemsNotNull(ms, i -> String.format("ms[%d] cannot be null", i));

        final int size = ms.length;
        if (size == 1) {
            return ms[0];
        }


        int i = 1;
        M result = null;
        for (var m : ms) {
            if (i == 1) /* first */ {
                result = m;
            } else {
                result = wrap.apply(result, m);
            }
            i++;
        }

        return result;
    }

    static <C extends Middleware.Context<C>, M extends Middleware<C>> M compose(
            BiFunction<M, M, M> wrap,
            List<M> ms
    ) {
        Args.notNull(wrap, "wrap cannot be null");
        Args.notEmpty(ms, "ms cannot be empty");
        Args.itemsNotNull(ms, i -> String.format("ms[%d] cannot be null", i));

        final int size = ms.size();
        if (size == 1) {
            return ms.getFirst();
        }


        int i = 1;
        M result = null;
        for (var m : ms) {
            if (i == 1) /* first */ {
                result = m;
            } else {
                result = wrap.apply(result, m);
            }
            i++;
        }

        return result;
    }
}
