package com.xyzwps.website.modules.debug;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.middleware.Router;
import com.xyzwps.website.common.JSON;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.HashMap;

import static com.xyzwps.lib.express.HttpStatus.*;

@Singleton
public class DebugRouterBuilder {

    public final Router router;

    @Inject
    DebugRouterBuilder() {
        this.router = new Router()
                .get("", (req, resp, next) -> {
                    req.attribute("haha", "haha");

                    resp.status(OK).header(HttpHeaders.CONTENT_TYPE, "application/json");

                    var map = new HashMap<String, Object>();
                    map.put("protocol", req.protocol());
                    map.put("method", req.method());
                    map.put("path", req.path());
                    map.put("headers", req.headers());
                    map.put("searchParams", req.searchParams());
                    map.put("attributes", req.attributes());

                    resp.send(JSON.stringify(map, true).getBytes());
                });
    }
}