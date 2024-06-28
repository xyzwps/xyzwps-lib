package com.xyzwps.website.common;

import com.xyzwps.lib.express.HttpContext;
import com.xyzwps.lib.express.HttpMiddleware;

@FunctionalInterface
public interface JsonHandler extends HttpMiddleware {

    <T> T handle(HttpContext context);

    @Override
    default void call(HttpContext context) {
        var result = handle(context);
        var resp = context.response();
        resp.ok();
        resp.headers().set("Content-Type", "application/json");
        resp.send(JSON.stringify(result).getBytes());
    }
}
