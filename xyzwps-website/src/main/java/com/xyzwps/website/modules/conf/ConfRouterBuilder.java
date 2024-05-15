package com.xyzwps.website.modules.conf;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.middleware.BasicAuth;
import com.xyzwps.lib.express.middleware.Router;
import com.xyzwps.website.common.JSON;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ConfRouterBuilder {

    public final Router router;

    @Inject
    ConfRouterBuilder(Configurations conf) {
        this.router = new Router()
                .get("", (ctx) -> {
                    var resp = ctx.response();
                    resp.ok();
                    resp.headers().set(HttpHeaders.CONTENT_TYPE, "application/json");
                    var map = new HashMap<String, Object>();
                    map.put("name", conf.getAppName());
                    resp.send(JSON.stringify(map, true).getBytes());
                });
    }
}