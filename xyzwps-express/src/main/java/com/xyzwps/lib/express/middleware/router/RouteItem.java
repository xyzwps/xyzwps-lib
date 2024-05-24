package com.xyzwps.lib.express.middleware.router;

import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpMiddleware;

sealed interface RouteItem {
    /**
     * @param url
     * @param method      match any of methods if it is null
     * @param middlewares
     */
    record Handler(HPath url, HttpMethod method, HttpMiddleware[] middlewares) implements RouteItem {
    }

    record Nest(HPath prefix, NestRouter router) implements RouteItem {
    }

    record Use(HttpMiddleware mw) implements RouteItem {
    }
}