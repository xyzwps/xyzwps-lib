package com.xyzwps.lib.express.middleware;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.core.HttpMethod;
import com.xyzwps.lib.express.core.HttpMiddleware;
import com.xyzwps.lib.express.core.HPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Router {

    private final List<RouteItem> items = new ArrayList<>();

    private int matchStart = 0;

    /**
     * @param method match all if it is null
     */
    public Router handle(HttpMethod method, String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        Args.notNull(mw0, "The first middleware cannot be null");
        Args.allNotNull(mws, "Middlewares cannot be null");

        HttpMiddleware[] mergedMws = new HttpMiddleware[mws.length + 1];
        System.arraycopy(mws, 0, mergedMws, 1, mws.length);
        mergedMws[0] = mw0;
        items.add(new RouteItem.Handler(HPath.from(url), method, mergedMws));
        return this;
    }

    public Router all(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(null, url, mw0, mws);
    }

    public Router get(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return this.handle(HttpMethod.GET, url, mw0, mws);
    }

    public Router post(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return this.handle(HttpMethod.POST, url, mw0, mws);
    }

    private void setMatchStart(int matchStart) {
        this.matchStart = matchStart;
        for (var item : items) {
            if (item instanceof RouteItem.Nest nest) {
                nest.router.setMatchStart(matchStart + nest.prefix.length());
            }
        }
    }

    public Router nest(String prefix, Router router) {
        Args.notNull(router, "Nested router cannot be null");
        Args.notNull(prefix, "Prefix cannot be null");

        if (prefix.contains("**")) {
            throw new IllegalArgumentException("Prefix cannot contains '**'");
        }

        var segmentedPrefix = HPath.from(prefix);
        if (segmentedPrefix.length() == 0) {
            throw new IllegalArgumentException("Prefix cannot be empty");
        }

        router.setMatchStart(this.matchStart + segmentedPrefix.length());
        items.add(new RouteItem.Nest(segmentedPrefix, router));
        return this;
    }

    public Router use(HttpMiddleware mw) {
        Args.notNull(mw, "Middleware cannot be null");

        items.add(new RouteItem.Use(mw));
        return this;
    }

    List<HttpMiddleware> match(HttpMethod method, String[] path) {
        List<HttpMiddleware> mws = new ArrayList<>();
        match(method, path, this, mws);
        return mws;
    }

    private static void match(HttpMethod method, String[] path, Router router, List<HttpMiddleware> mws) {
        for (var item : router.items) {
            switch (item) {
                case RouteItem.Handler handler -> {
                    if (handler.method == null || handler.method == method) {
                        if (handler.url.match(path, router.matchStart)) {
                            mws.addAll(Arrays.asList(handler.middlewares));
                            return;
                        }
                    }
                }
                case RouteItem.Use use -> mws.add(use.mw);
                case RouteItem.Nest nest -> {
                    if (nest.prefix.isPrefixOf(path, router.matchStart)) {
                        match(method, path, nest.router, mws);
                        return;
                    }
                }
            }
        }
    }

    public HttpMiddleware routes() {
        return (req, resp, next) -> {
            var path = HPath.pathToSegmentStrings(req.path());
            var mws = this.match(req.method(), path);
            var composed = HttpMiddleware.compose(mws);
            composed.call(req, resp, next);
        };
    }

    sealed interface RouteItem {
        /**
         * @param url
         * @param method      match any of methods if it is null
         * @param middlewares
         */
        record Handler(HPath url, HttpMethod method, HttpMiddleware[] middlewares) implements RouteItem {
        }

        record Nest(HPath prefix, Router router) implements RouteItem {
        }

        record Use(HttpMiddleware mw) implements RouteItem {
        }
    }
}
