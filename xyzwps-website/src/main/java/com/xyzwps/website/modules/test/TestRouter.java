package com.xyzwps.website.modules.test;

import com.xyzwps.lib.express.filter.BasicAuth;
import com.xyzwps.lib.express.filter.Router;
import com.xyzwps.website.common.JSON;
import com.xyzwps.website.conf.Configurations;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
@Singleton
public class TestRouter extends Router.Nest {

    public TestRouter(Configurations conf, BasicAuth basicAuth, TestDao testDao) {
        this.get("count", new TestCountFilter(1) + new TestCountFilter(2) + new HelloWorldFilter())
                .get("conf", (req, resp, next) -> {
                    var map = new HashMap<String, Object>();
                    map.put("name", conf.getAppName());
                    resp.sendJson(map);
                })
                .post("manifold", (req, resp, next) -> {
                    var body = req.json(TestManifoldPayload.class, JSON.JM);
                    log.info("body: {}", body.getHello());
                    resp.sendJson(body);
                })
                .get("/auth", basicAuth + new HelloWorldFilter());
    }

}
