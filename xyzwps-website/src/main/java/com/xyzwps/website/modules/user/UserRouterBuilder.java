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
                .get("/{id}", (req, resp, next) -> {
                    resp.status(OK).header("Content-Type", "application/json");
                    resp.send("{\"msg\":\"get user\"}".getBytes());
                })
                .post("/{id}", jsonParser.json(Person.class), (req, resp, next) -> {
                    var body = req.body();
                    if (body instanceof Person p) {
                        resp.status(OK);
                        resp.header(HttpHeaders.CONTENT_TYPE, "application/json");
                        resp.send(("{\"name\":\"" + p.name() + "\"}").getBytes());
                    } else {
                        resp.status(INTERNAL_SERVER_ERROR);
                        resp.header(HttpHeaders.CONTENT_TYPE, "application/json");
                        resp.send(("{\"error\":true}").getBytes());
                    }
                })
                .use((req, resp, next) -> {
                    System.out.println(" > ready to get posts");
                    next.call();
                })
                .get("/{id}/posts", (req, resp, next) -> {
                    resp.status(OK).header("Content-Type", "application/json");
                    System.out.println(" > posts gotten");
                    resp.send("{\"msg\":\"get user posts\"}".getBytes());
                });
    }
}
