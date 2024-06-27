package com.xyzwps.website;

import com.xyzwps.lib.express.ServerConfig;
import com.xyzwps.lib.express.middleware.Static;
import com.xyzwps.lib.express.server.bio.BioServer;
import com.xyzwps.website.middleware.LogRequestCostMiddleware;
import com.xyzwps.website.middleware.SpaFallbackMiddleware;
import com.xyzwps.website.modules.IndexRouterBuilder;
import com.xyzwps.website.modules.conf.Configurations;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HttpServerLayer {

    private static final Logger log = Logger.getLogger(LogRequestCostMiddleware.class);

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
        log.infof("=====> server is listening at %s <=====\n", serverConfig.port);
        new BioServer().start(this.serverConfig);
    }
}
