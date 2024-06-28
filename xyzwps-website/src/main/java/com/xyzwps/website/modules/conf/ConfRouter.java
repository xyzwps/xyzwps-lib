package com.xyzwps.website.modules.conf;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.middleware.router.NestRouter;
import com.xyzwps.website.common.JSON;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.function.Consumer;

@Singleton
public class ConfRouter implements Consumer<NestRouter> {

    public final Configurations conf;

    ConfRouter(Configurations conf) {
        this.conf = conf;
    }

    @Override
    public void accept(NestRouter nestRouter) {
        nestRouter
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