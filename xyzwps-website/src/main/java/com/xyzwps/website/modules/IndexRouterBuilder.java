package com.xyzwps.website.modules;

import com.xyzwps.lib.express.middleware.router.Router;
import com.xyzwps.lib.express.middleware.router.RouterMiddleware;
import com.xyzwps.website.modules.conf.ConfRouter;
import com.xyzwps.website.modules.debug.DebugRouter;
import com.xyzwps.website.modules.user.UserRouter;
import jakarta.inject.Singleton;

@Singleton
public class IndexRouterBuilder {

    public final RouterMiddleware router;

    public IndexRouterBuilder(ConfRouter confBuilder,
                              DebugRouter debugRouter,
                              UserRouter userRouter) {

        var rooter = new Router()
                .get("/api/hello/world", (ctx) -> {
                    var resp = ctx.response();
                    resp.ok();
                    resp.headers().set("Content-Type", "application/json");
                    resp.send("[\"Hello\":\"World\"]".getBytes());
                })
                .nest("/api/conf", confBuilder)
                .nest("/api/debug", debugRouter)
                .nest("/api/users", userRouter);

        this.router = new RouterMiddleware(rooter);
    }
}
