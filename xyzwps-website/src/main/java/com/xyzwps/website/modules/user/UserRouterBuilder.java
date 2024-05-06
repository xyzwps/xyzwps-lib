package com.xyzwps.website.modules.user;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.middleware.JsonParser;
import com.xyzwps.lib.express.middleware.Router;
import com.xyzwps.website.common.JSON;
import com.xyzwps.website.Person;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.xyzwps.lib.express.HttpStatus.*;

@Singleton
public class UserRouterBuilder {

    public final Router router;

    @Inject
    UserRouterBuilder() {
        var jsonParser = new JsonParser(JSON.OM);

        this.router = new Router()
                .get("/{id}", (ctx) -> {
                    var resp = ctx.response();
                    resp.ok();
                    resp.headers().set("Content-Type", "application/json");
                    resp.send("{\"msg\":\"get user\"}".getBytes());
                })
                .post("/{id}", jsonParser.json(Person.class), (ctx) -> {
                    var req = ctx.request();
                    var resp = ctx.response();

                    var body = req.body();
                    if (body instanceof Person p) {
                        resp.ok();
                        resp.headers().set(HttpHeaders.CONTENT_TYPE, "application/json");
                        resp.send(("{\"name\":\"" + p.name() + "\"}").getBytes());
                    } else {
                        resp.status(INTERNAL_SERVER_ERROR);
                        resp.headers().set(HttpHeaders.CONTENT_TYPE, "application/json");
                        resp.send(("{\"error\":true}").getBytes());
                    }
                })
                .use((ctx) -> {
                    System.out.println(" > ready to get posts");
                    ctx.next();
                })
                .get("/{id}/posts", (ctx) -> {
                    var resp = ctx.response();
                    resp.ok();
                    resp.headers().set("Content-Type", "application/json");
                    System.out.println(" > posts gotten");
                    resp.send("{\"msg\":\"get user posts\"}".getBytes());
                });
    }
}
