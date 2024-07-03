package com.xyzwps.website;

import com.xyzwps.lib.express.ServerConfig;
import com.xyzwps.lib.express.middleware.Static;
import com.xyzwps.lib.express.server.bio.BioServer;
import com.xyzwps.website.middleware.LogRequestCostMiddleware;
import com.xyzwps.website.middleware.SpaFallbackMiddleware;
import com.xyzwps.website.modules.IndexRouter;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

@Singleton
public class HttpServerLayer {

    private static final Logger log = Logger.getLogger(LogRequestCostMiddleware.class);

    private final ServerConfig serverConfig;

    public HttpServerLayer(IndexRouter routerBuilder,
                           Configurations conf,
                           LogRequestCostMiddleware logRequestCostMiddleware,
                           SpaFallbackMiddleware spaFallbackMiddleware) {
        this.serverConfig = ServerConfig.create()
                .port(conf.getServerPort())
                .use(logRequestCostMiddleware)
                .use(new Static(conf.getRouterStaticDirectory()).serve())
                .use(spaFallbackMiddleware)
                .use(routerBuilder.toFilter());
    }

    public void start() {
        log.infof("=====> server is listening at %s <=====\n", serverConfig.port);
        new BioServer().start(this.serverConfig);
    }
}
