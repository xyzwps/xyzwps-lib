package com.xyzwps.website.modules.debug;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.middleware.BasicAuth;
import com.xyzwps.lib.express.middleware.Router;
import com.xyzwps.website.common.JSON;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class DebugRouterBuilder {

    public final Router router;

    @Inject
    DebugRouterBuilder(BasicAuth basicAuth) {
        this.router = new Router()
                .get("", (ctx) -> {
                    var req = ctx.request();
                    var resp = ctx.response();

                    ctx.attribute("haha", "haha");

                    resp.ok();
                    resp.headers().set(HttpHeaders.CONTENT_TYPE, "application/json");

                    var map = new HashMap<String, Object>();
                    map.put("protocol", req.protocol());
                    map.put("method", req.method());
                    map.put("path", req.path());
                    map.put("headers", req.headers());
                    map.put("searchParams", req.searchParams());
                    map.put("attributes", ctx.attributes());

                    resp.send(JSON.stringify(map, true).getBytes());
                })
                .get("/auth", basicAuth, (ctx) -> {
                    var resp = ctx.response();

                    resp.ok();
                    resp.headers().set(HttpHeaders.CONTENT_TYPE, "application/json");
                    resp.send(JSON.stringify(Map.of("ok", true), true).getBytes());
                });
    }
}