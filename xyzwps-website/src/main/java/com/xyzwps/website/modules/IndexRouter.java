package com.xyzwps.website.modules;

import com.xyzwps.lib.express.filter.Router;
import com.xyzwps.website.modules.conf.ConfRouter;
import com.xyzwps.website.modules.debug.DebugRouter;
import com.xyzwps.website.modules.user.UserRouter;
import jakarta.inject.Singleton;

@Singleton
public class IndexRouter extends Router {

    public IndexRouter(ConfRouter confRouter,
                       DebugRouter debugRouter,
                       UserRouter userRouter) {

        this.get("/api/hello/world", (req, resp) -> {
                    resp.ok();
                    resp.headers().set("Content-Type", "application/json");
                    resp.send("[\"Hello\",\"World\"]".getBytes());
                })
                .nest("/api/conf", confRouter)
                .nest("/api/debug", debugRouter)
                .nest("/api/users", userRouter);
    }
}
