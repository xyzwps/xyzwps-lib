package com.xyzwps.website.modules;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpStatus;
import com.xyzwps.lib.express.middleware.Router;
import com.xyzwps.website.modules.debug.DebugRouterBuilder;
import com.xyzwps.website.modules.user.UserRouterBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IndexRouterBuilder {

    public final Router router;

    @Inject
    public IndexRouterBuilder(DebugRouterBuilder debugRouter, UserRouterBuilder userRouter) {
        this.router = new Router()
                .get("/hello/world", (req, resp, next) -> {
                    resp.ok();
                    resp.headers().set("Content-Type", "application/json");
                    resp.send("[\"Hello\":\"World\"]".getBytes());
                })
                .nest("/debug", debugRouter.router)
                .nest("/users", userRouter.router)
                .all("/**", (req, resp, next) -> {
                    resp.headers().set(HttpHeaders.CONTENT_TYPE, "application/json");
                    resp.status(HttpStatus.NOT_FOUND);
                    resp.send("{\"status\":404}".getBytes());
                });
    }
}
