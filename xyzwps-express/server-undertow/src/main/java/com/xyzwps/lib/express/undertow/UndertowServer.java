package com.xyzwps.lib.express.undertow;

import com.xyzwps.lib.express.core.HttpMiddleware;
import com.xyzwps.lib.express.core.Middleware2Composer;
import com.xyzwps.lib.express.core.Next;
import com.xyzwps.lib.express.core.Server;
import io.undertow.Undertow;

public final class UndertowServer implements Server {

    private Undertow undertow;

    private HttpMiddleware middleware = HttpMiddleware.DO_NOTHING;

    public Server use(HttpMiddleware mw) {
        this.middleware = Middleware2Composer.compose2(middleware, mw)::call;
        return this;
    }

    /**
     * TODO: 不能重复
     *
     * @param port
     */
    @Override
    public void listen(int port) {
        // TODO: lock

        this.undertow = Undertow.builder()
                .addHttpListener(port, "localhost")
                .setHandler((exchange -> {
                    var req = new UndertowHttpRequest(exchange);
                    var resp = new UndertowHttpResponse(exchange);
                    this.middleware.call(req, resp, Next.EMPTY);
                }))
                .build();

        this.undertow.start();
        // TODO:
    }
}
