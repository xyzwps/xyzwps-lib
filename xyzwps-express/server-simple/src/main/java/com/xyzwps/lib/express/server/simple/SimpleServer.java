package com.xyzwps.lib.express.server.simple;

import com.sun.net.httpserver.HttpServer;
import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.lib.express.Server;
import com.xyzwps.lib.express.util.Middleware2Composer;

import java.io.IOException;
import java.io.UncheckedIOException;

public class SimpleServer implements Server {

    private final HttpServer httpServer;

    private HttpMiddleware middleware = HttpMiddleware.DO_NOTHING;

    SimpleServer() {
        try {
            this.httpServer = HttpServer.create();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        this.httpServer.createContext("/debug", (exchange) -> {

        });
    }

    @Override
    public Server use(HttpMiddleware mw) {
        this.middleware = Middleware2Composer.compose2(middleware, mw)::call;
        return this;
    }

    @Override
    public Server listen(int port) {
        return null;
    }

    @Override
    public void start() {

    }
}
