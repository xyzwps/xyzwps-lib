package com.xyzwps.website.filter;

import com.xyzwps.lib.express.Handler;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.website.common.JSON;

@FunctionalInterface
public interface JsonHandler extends Handler {

    Object handleJson(HttpRequest request, HttpResponse response);

    @Override
    default void handle(HttpRequest req, HttpResponse resp) {
        var result = handleJson(req, resp);
        resp.ok();
        resp.headers().set("Content-Type", "application/json");
        resp.send(JSON.stringify(result).getBytes());
    }
}
