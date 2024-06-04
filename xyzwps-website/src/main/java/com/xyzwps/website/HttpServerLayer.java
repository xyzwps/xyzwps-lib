package com.xyzwps.website;

import com.xyzwps.lib.express.Log;
import com.xyzwps.lib.express.ServerConfig;
import com.xyzwps.lib.express.middleware.Static;
import com.xyzwps.lib.express.server.bio.BioServer;
import com.xyzwps.website.middleware.LogRequestCostMiddleware;
import com.xyzwps.website.middleware.SpaFallbackMiddleware;
import com.xyzwps.website.modules.IndexRouterBuilder;
import com.xyzwps.website.modules.conf.Configurations;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HttpServerLayer {

    private final ServerConfig serverConfig;

    @Inject
    public HttpServerLayer(IndexRouterBuilder routerBuilder,
                           Configurations conf,
                           LogRequestCostMiddleware logRequestCostMiddleware,
                           SpaFallbackMiddleware spaFallbackMiddleware) {
        this.serverConfig = ServerConfig.create()
                .port(conf.getServerPort())
                .use(logRequestCostMiddleware)
                .use(new Static(conf.getRouterStaticDirectory()).serve())
                .use(spaFallbackMiddleware)
                .use(routerBuilder.router);
    }

    public void start() {
        Log.infof("=====> server is listening at {} <=====\n", serverConfig.port);
        new BioServer().start(this.serverConfig);
    }
}
