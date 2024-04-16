package com.xyzwps.website;

import com.xyzwps.lib.express.middleware.Router;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRouterBuilder {

    public final Router router;

    @Inject
    UserRouterBuilder() {
        this.router = new Router()
                .get("/{id}", (req, resp, next) -> {
                    resp.status(200).header("Content-Type", "application/json");
                    resp.send("{\"msg\":\"get user\"}".getBytes());
                })
                .use((req, resp, next) -> {
                    System.out.println(" > ready to get posts");
                    next.call();
                })
                .get("/{id}/posts", (req, resp, next) -> {
                    resp.status(200).header("Content-Type", "application/json");
                    System.out.println(" > posts gotten");
                    resp.send("{\"msg\":\"get user posts\"}".getBytes());
                });
    }
}
