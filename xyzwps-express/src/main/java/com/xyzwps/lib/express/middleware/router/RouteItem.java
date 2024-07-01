package com.xyzwps.lib.express.middleware.router;

import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpMiddleware;

sealed interface RouteItem {
    /**
     * @param url
     * @param method      match any of methods if it is null
     * @param middleware
     */
    record Handler(String url, HttpMethod method, HttpMiddleware middleware) implements RouteItem {
    }

    record Nest(String prefix, Router router) implements RouteItem {
    }

    record Use(HttpMiddleware mw) implements RouteItem {
    }
}