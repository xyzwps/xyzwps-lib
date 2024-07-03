package com.xyzwps.website.modules.conf;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.filter.Router;
import com.xyzwps.website.conf.Configurations;
import com.xyzwps.website.common.JSON;
import jakarta.inject.Singleton;

import java.util.HashMap;

@Singleton
public class ConfRouter extends Router {

    public ConfRouter(Configurations conf) {
        this.get("", (req, resp) -> {
            resp.ok();
            resp.headers().set(HttpHeaders.CONTENT_TYPE, "application/json");
            var map = new HashMap<String, Object>();
            map.put("name", conf.getAppName());
            resp.send(JSON.stringify(map, true).getBytes());
        });
    }
}