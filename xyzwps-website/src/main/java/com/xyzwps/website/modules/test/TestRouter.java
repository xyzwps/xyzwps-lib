package com.xyzwps.website.modules.test;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.filter.BasicAuth;
import com.xyzwps.lib.express.filter.Router;
import com.xyzwps.website.common.JSON;
import com.xyzwps.website.conf.Configurations;
import com.xyzwps.website.db.MainDatabase;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class TestRouter extends Router.Nest {

    public TestRouter(Configurations conf, BasicAuth basicAuth, MainDatabase maindb) {
        this.get("count", new TestCountFilter(2).andThen(new TestCountFilter(3)).andThen((req, resp, next) -> {
                    resp.ok();
                    resp.send("Hello, World!".getBytes());
                }))
                .get("conf", (req, resp, next) -> {
                    resp.ok();
                    resp.headers().set(HttpHeaders.CONTENT_TYPE, "application/json");
                    var map = new HashMap<String, Object>();
                    map.put("name", conf.getAppName());
                    resp.send(JSON.stringify(map, true).getBytes());
                })
                .get("/:id", (req, resp, next) -> {
                    req.attribute("haha", "ha\nha");

                    var map = new HashMap<String, Object>();

                    maindb.tx(tx -> {
                        var dao = tx.createDao(TestDao.class);
                        map.put("jdbc", dao.count());
                    });

                    resp.ok();
                    resp.headers().set(HttpHeaders.CONTENT_TYPE, "application/json");


                    map.put("protocol", req.protocol());
                    map.put("method", req.method());
                    map.put("path", req.path());
                    map.put("headers", req.headers());
                    map.put("searchParams", req.searchParams());
                    map.put("attributes", req.attributes());
                    map.put("pathVars", req.pathVariables());
                    map.put("date", new Date());
                    map.put("localDateTime", LocalDateTime.now());

                    resp.send(JSON.stringify(map, true).getBytes());
                })
                .get("/auth", basicAuth.andThen((req, resp, next) -> {
                    resp.ok();
                    resp.headers().set(HttpHeaders.CONTENT_TYPE, "application/json");
                    resp.send(JSON.stringify(Map.of("ok", true), true).getBytes());
                }));
    }

}
