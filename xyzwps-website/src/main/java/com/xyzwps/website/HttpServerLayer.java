package com.xyzwps.website;

import com.xyzwps.lib.express.ServerConfig;
import com.xyzwps.lib.express.filter.Static;
import com.xyzwps.lib.express.server.bio.BioServer;
import com.xyzwps.website.conf.Configurations;
import com.xyzwps.website.filter.LogRequestCostFilter;
import com.xyzwps.website.filter.OpenApiFilter;
import com.xyzwps.website.filter.SpaFallbackFilter;
import com.xyzwps.website.modules.IndexRouter;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HttpServerLayer {

    private static final Logger log = LoggerFactory.getLogger(LogRequestCostFilter.class);

    private final ServerConfig serverConfig;

    public HttpServerLayer(IndexRouter routerBuilder,
                           Configurations conf,
                           OpenApiFilter openApi,
                           LogRequestCostFilter logRequestCostFilter,
                           SpaFallbackFilter spaFallbackFilter) {
        this.serverConfig = ServerConfig.create()
                .port(conf.getServerPort())
                .use(logRequestCostFilter)
                .use(openApi)
                .use(new Static(conf.getRouterStaticDirectory()).serve())
                .use(spaFallbackFilter)
                .use(routerBuilder::filter);
    }

    public void start() {
        log.info("=====> server is listening at {} <=====", serverConfig.port);
        new BioServer().start(this.serverConfig);
    }
}
