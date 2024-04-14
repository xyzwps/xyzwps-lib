package com.xyzwps.lib.express.middleware;

import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpMiddleware;

import java.util.Arrays;

public class Router {

    public Router handle(HttpMethod method, String url, HttpMiddleware... mws) {
        var segments = Arrays.stream(url.trim().split("/")).filter(it -> !it.isEmpty()).toList();
        throw new UnsupportedOperationException();
    }

    public Router nest(Router router) {
        throw new UnsupportedOperationException();
    }

    public HttpMiddleware routes() {
        throw new UnsupportedOperationException();
    }
}
