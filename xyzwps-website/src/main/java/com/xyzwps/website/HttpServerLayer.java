package com.xyzwps.website;

import com.xyzwps.lib.express.Server;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HttpServerLayer {

    private final Server server;

    @Inject
    public HttpServerLayer(IndexRouterBuilder routerBuilder,
                           LogRequestCostMiddleware logRequestCostMiddleware) {
        this.server = new Server()
                .use(logRequestCostMiddleware)
                .use(routerBuilder.router.routes());
    }

    public void listen(int port) {
        System.out.printf("=====> server is listening at %d <=====\n", port);
        server.listen(port);
    }
}
