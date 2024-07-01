package com.xyzwps.lib.express.middleware.router;

import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpMiddleware;

import java.util.ArrayList;
import java.util.List;

import static com.xyzwps.lib.express.HttpMiddleware.*;

public class Router {

    private final List<RouteItem> items = new ArrayList<>();

    public Router handle(HttpMethod method, String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        if (method == HttpMethod.CONNECT) {
            throw new IllegalArgumentException("Unsupported method: " + method);
        }

        items.add(new RouteItem.Handler(url, method, compose2(mw0, compose(mws))));
        return this;
    }

    public Router use(HttpMiddleware mw) {
        items.add(new RouteItem.Use(mw));
        return this;
    }

    public Router nest(String prefix, Router router) {
        items.add(new RouteItem.Nest(prefix, router));
        return this;
    }

    List<RouteItem.Handler> toHandlers(String prefix, HttpMiddleware parentMiddleware) {
        List<List<HttpMiddleware>> useItems = new ArrayList<>();
        final int size = items.size();
        for (int i = 0; i < size; i++) {
            var item = items.get(i);
            List<HttpMiddleware> prev = i == 0 ? List.of() : useItems.getLast();
            if (item instanceof RouteItem.Use use) {
                List<HttpMiddleware> curr = new ArrayList<>(prev);
                curr.add(use.mw());
                useItems.add(curr);
            } else {
                useItems.add(prev);
            }
        }


        List<RouteItem.Handler> handlers = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            var userItemsI = useItems.get(i);
            switch (items.get(i)) {
                case RouteItem.Handler handler ->
                        handlers.add(new RouteItem.Handler(prefix + '/' + handler.url(), handler.method(),
                                compose(parentMiddleware, compose(userItemsI.toArray(EMPTY_MIDDLEWARES)), handler.middleware())));
                case RouteItem.Nest nest -> handlers.addAll(nest.router().toHandlers(prefix + '/' + nest.prefix(),
                        compose(parentMiddleware, compose(userItemsI.toArray(EMPTY_MIDDLEWARES)))));
                case RouteItem.Use ignored -> {
                }
            }
        }
        return handlers;
    }

    private static final HttpMiddleware[] EMPTY_MIDDLEWARES = new HttpMiddleware[0];


    public Router all(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(null, url, mw0, mws);
    }

    public Router get(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.GET, url, mw0, mws);
    }

    public Router post(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.POST, url, mw0, mws);
    }

    public Router put(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.PUT, url, mw0, mws);
    }

    public Router delete(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.DELETE, url, mw0, mws);
    }

    public Router patch(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.PATCH, url, mw0, mws);
    }

    public Router head(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.HEAD, url, mw0, mws);
    }

    public Router options(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.OPTIONS, url, mw0, mws);
    }

    public Router trace(String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        return handle(HttpMethod.TRACE, url, mw0, mws);
    }

}
