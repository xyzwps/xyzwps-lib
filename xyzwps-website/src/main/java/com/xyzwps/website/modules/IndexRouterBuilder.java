package com.xyzwps.website.modules;

import com.xyzwps.lib.express.core.HttpStatus;
import com.xyzwps.lib.express.server.HttpHeaders;
import com.xyzwps.lib.express.middleware.Router;
import com.xyzwps.website.modules.user.UserRouterBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IndexRouterBuilder {

    public final Router router;

    @Inject
    public IndexRouterBuilder(UserRouterBuilder userRouter) {
        this.router = new Router()
                .get("/hello/world", (req, resp, next) -> {
                    resp.status(HttpStatus.OK).header("Content-Type", "application/json");
                    resp.send("[\"Hello\":\"World\"]".getBytes());
                })
                .nest("/users", userRouter.router)
                .all("/**", (req, resp, next) -> {
                    resp.header(HttpHeaders.CONTENT_TYPE, "application/json");
                    resp.status(HttpStatus.NOT_FOUND);
                    resp.send("{\"status\":404}".getBytes());
                });
    }
}
