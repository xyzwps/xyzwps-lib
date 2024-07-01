package com.xyzwps.lib.express;

import com.xyzwps.lib.express.util.Middleware;

public interface HttpMiddleware extends Middleware<HttpContext> {

    HttpMiddleware DO_NOTHING = HttpContext::next;

    static HttpMiddleware compose(HttpMiddleware... mws) {
        return Middleware.compose(HttpMiddleware::compose2, mws);
    }

    static HttpMiddleware compose2(HttpMiddleware m1, HttpMiddleware m2) {
        return context -> m1.call(new HttpContext(m2, context));
    }
}
