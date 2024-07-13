package com.xyzwps.website.modules.test;

import com.xyzwps.lib.express.filter.BasicAuth;
import com.xyzwps.lib.express.filter.Router;
import com.xyzwps.website.conf.Configurations;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

import static manifold.collections.api.range.RangeFun.to;

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
                .get("manifold", (req, res, next) -> {
                    for (var i : 1to 5) {
                        System.out.println(i);
                    }
                    res.ok();
                    res.send("Hello, Manifold!".getBytes());
                })
                .post("manifold", (req, resp, next) -> {
                    var body = req.json(TestManifoldPayload.class);
                    log.info("body: {}", body.getHello());
                    resp.sendJson(body);
                })
                .get("/:id", (req, resp, next) -> {
                    req.attribute("haha", "ha\nha");

                    var map = new HashMap<String, Object>();

                    map.put("jdbc", testDao.count());
                    map.put("protocol", req.protocol());
                    map.put("method", req.method());
                    map.put("path", req.path());
                    map.put("headers", req.headers());
                    map.put("searchParams", req.searchParams());
                    map.put("attributes", req.attributes());
                    map.put("pathVars", req.pathVariables());
                    map.put("date", new Date());
                    map.put("localDateTime", LocalDateTime.now());

                    resp.sendJson(map);
                })
                .get("/auth", basicAuth + new HelloWorldFilter());
    }

}
