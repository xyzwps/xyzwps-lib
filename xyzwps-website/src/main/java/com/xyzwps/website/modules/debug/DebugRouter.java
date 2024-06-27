package com.xyzwps.website.modules.debug;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.middleware.BasicAuth;
import com.xyzwps.lib.express.middleware.router.NestRouter;
import com.xyzwps.website.common.JSON;
import com.xyzwps.website.db.MainDatabase;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Singleton
public class DebugRouter implements Consumer<NestRouter> {

    public final BasicAuth basicAuth;

    public final MainDatabase maindb;

    @Inject
    DebugRouter(BasicAuth basicAuth, MainDatabase maindb) {
        this.basicAuth = basicAuth;
        this.maindb = maindb;
    }

    @Override
    public void accept(NestRouter nestRouter) {
        nestRouter
                .get("/{id}", (ctx) -> {
                    var req = ctx.request();
                    var resp = ctx.response();

                    ctx.attribute("haha", "ha\nha");

                    var map = new HashMap<String, Object>();

                    maindb.tx(tx -> {
                        var dao = tx.createDao(DebugDao.class);
                        map.put("jdbc", dao.count());
                    });

                    resp.ok();
                    resp.headers().set(HttpHeaders.CONTENT_TYPE, "application/json");


                    map.put("protocol", req.protocol());
                    map.put("method", req.method());
                    map.put("path", req.path());
                    map.put("headers", req.headers());
                    map.put("searchParams", req.searchParams());
                    map.put("attributes", ctx.attributes());
                    map.put("pathVars", req.pathVariables());
                    map.put("date", new Date());
                    map.put("localDateTime", LocalDateTime.now());

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