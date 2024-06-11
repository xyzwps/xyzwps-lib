package com.xyzwps.website.modules.user;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.Log;
import com.xyzwps.lib.express.middleware.JsonParser;
import com.xyzwps.lib.express.middleware.router.NestRouter;
import com.xyzwps.website.common.JSON;
import com.xyzwps.website.Person;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.function.Consumer;

import static com.xyzwps.lib.express.HttpStatus.*;

@Singleton
public class UserRouter implements Consumer<NestRouter> {

    private final JsonParser json = new JsonParser(JSON.JM);

    @Inject
    UserRouter() {
    }

    @Override
    public void accept(NestRouter nestRouter) {
        nestRouter
                .get("/{id}", (ctx) -> {
                    var resp = ctx.response();
                    resp.ok();
                    resp.headers().set("Content-Type", "application/json");
                    resp.send("{\"msg\":\"get user\"}".getBytes());
                })
                .post("/{id}", json.json(Person.class), (ctx) -> {
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
                    Log.infof(" > ready to get posts");
                    ctx.next();
                })
                .get("/{id}/posts", (ctx) -> {
                    var resp = ctx.response();
                    resp.ok();
                    resp.headers().set("Content-Type", "application/json");
                    Log.infof(" > posts gotten");
                    resp.send("{\"msg\":\"get user posts\"}".getBytes());
                });
    }
}
