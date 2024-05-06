package com.xyzwps.website;

import com.xyzwps.lib.express.ServerConfig;
import com.xyzwps.lib.express.middleware.Static;
import com.xyzwps.lib.express.server.craft.CraftServer;
import com.xyzwps.lib.express.server.undertow.UndertowServer;
import com.xyzwps.website.middleware.LogRequestCostMiddleware;
import com.xyzwps.website.modules.IndexRouterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HttpServerLayer {

    private static final Logger log = LoggerFactory.getLogger(HttpServerLayer.class);

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
        log.info("=====> server is listening at {} <=====\n", config.port);
        new UndertowServer().start(this.config);
    }
}
