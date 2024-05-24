package com.xyzwps.lib.express.middleware.router;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpMiddleware;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


abstract class Router<T extends Router<T>> {
    protected final int matchStart;

    protected final List<RouteItem> items = new ArrayList<>(); // TODO: 并发安全

    protected Router(int matchStart) {
        this.matchStart = matchStart;
    }


    public abstract T handle(HttpMethod method, String url, HttpMiddleware mw0, HttpMiddleware... mws);


    protected void handle0(HttpMethod method, String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        Args.notNull(mw0, "The first middleware cannot be null");
        Args.notNull(mws, "mws cannot be null");
        Args.itemsNotNull(mws, i -> "mws[" + i + "] cannot be null");

        HttpMiddleware[] mergedMws = new HttpMiddleware[mws.length + 1];
        System.arraycopy(mws, 0, mergedMws, 1, mws.length);
        mergedMws[0] = mw0;
        items.add(new RouteItem.Handler(HPath.from(url), method, mergedMws));
    }

    public T all(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(null, url, mw0, mws);
    }

    public T get(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.GET, url, mw0, mws);
    }

    public T post(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.POST, url, mw0, mws);
    }

    public T put(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.PUT, url, mw0, mws);
    }

    public T delete(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.DELETE, url, mw0, mws);
    }

    public T patch(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.PATCH, url, mw0, mws);
    }

    /**
     * TODO:
     */
    public T head(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.HEAD, url, mw0, mws);
    }

    /**
     * TODO:
     */
    public T connect(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.CONNECT, url, mw0, mws);
    }

    /**
     * TODO:
     */
    public T options(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.OPTIONS, url, mw0, mws);
    }

    /**
     * TODO:
     */
    public T trace(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.TRACE, url, mw0, mws);
    }

    public abstract T use(HttpMiddleware mw);

    protected void use0(HttpMiddleware mw) {
        Args.notNull(mw, "Middleware cannot be null");

        items.add(new RouteItem.Use(mw));
    }

    public abstract T nest(String prefix, Consumer<NestRouter> routerConsumer);

    protected void nest0(String prefix, Consumer<NestRouter> nestRouterBuilder) {
        Args.notNull(nestRouterBuilder, "nestRouterBuilder cannot be null");
        Args.notNull(prefix, "Prefix cannot be null");

        if (prefix.contains("**")) {
            throw new IllegalArgumentException("Prefix cannot contains '**'");
        }

        var segmentedPrefix = HPath.from(prefix);
        if (segmentedPrefix.length() == 0) {
            throw new IllegalArgumentException("Prefix cannot be empty");
        }

        var nestRouter = new NestRouter(this.matchStart + segmentedPrefix.length());
        nestRouterBuilder.accept(nestRouter);
        items.add(new RouteItem.Nest(segmentedPrefix, nestRouter));
    }
}
