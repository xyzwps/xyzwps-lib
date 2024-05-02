package com.xyzwps.website;

import com.xyzwps.lib.express.ServerConfig;
import com.xyzwps.lib.express.middleware.Static;
import com.xyzwps.lib.express.server.craft.CraftServer;
import com.xyzwps.lib.express.server.simple.SimpleServer;
import com.xyzwps.lib.express.server.undertow.UndertowServer;
import com.xyzwps.website.middleware.LogRequestCostMiddleware;
import com.xyzwps.website.modules.IndexRouterBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HttpServerLayer {

    private final ServerConfig config;

    @Inject
    public HttpServerLayer(IndexRouterBuilder routerBuilder,
                           LogRequestCostMiddleware logRequestCostMiddleware) {
        this.config = ServerConfig.create()
                .port(3000)
                .use(logRequestCostMiddleware)
                .use(new Static("/Users/weiliangyu").serve()) // TODO: xxx
                .use(routerBuilder.router.routes());
    }

    public void start() {
        System.out.printf("=====> server is listening at %d <=====\n", config.port);
        new CraftServer().start(this.config);
    }
}
