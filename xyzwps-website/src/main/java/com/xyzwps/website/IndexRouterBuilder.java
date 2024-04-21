package com.xyzwps.website;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.middleware.Router;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IndexRouterBuilder {

    public final Router router;

    @Inject
    public IndexRouterBuilder(UserRouterBuilder userRouter) {
        this.router = new Router()
                .get("/hello/world", (req, resp, next) -> {
                    resp.status(200).header("Content-Type", "application/json");
                    resp.send("[\"Hello\":\"World\"]".getBytes());
                })
                .nest("/users", userRouter.router)
                .all("/**", (req, resp, next) -> {
                    resp.header(HttpHeaders.CONTENT_TYPE, "application/json");
                    resp.status(404);
                    resp.send("{\"status\":404}".getBytes());
                });
    }
}
