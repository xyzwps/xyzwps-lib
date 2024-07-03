package com.xyzwps.website;

import com.xyzwps.lib.express.ServerConfig;
import com.xyzwps.lib.express.filter.Static;
import com.xyzwps.lib.express.server.bio.BioServer;
import com.xyzwps.website.filter.LogRequestCostFilter;
import com.xyzwps.website.filter.SpaFallbackFilter;
import com.xyzwps.website.modules.IndexRouter;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

@Singleton
public class HttpServerLayer {

    private static final Logger log = Logger.getLogger(LogRequestCostFilter.class);

    private final ServerConfig serverConfig;

    public HttpServerLayer(IndexRouter routerBuilder,
                           Configurations conf,
                           LogRequestCostFilter logRequestCostFilter,
                           SpaFallbackFilter spaFallbackFilter) {
        this.serverConfig = ServerConfig.create()
                .port(conf.getServerPort())
                .use(logRequestCostFilter)
                .use(new Static(conf.getRouterStaticDirectory()).serve())
                .use(spaFallbackFilter)
                .use(routerBuilder.toFilter());
    }

    public void start() {
        log.infof("=====> server is listening at %s <=====\n", serverConfig.port);
        new BioServer().start(this.serverConfig);
    }
}
