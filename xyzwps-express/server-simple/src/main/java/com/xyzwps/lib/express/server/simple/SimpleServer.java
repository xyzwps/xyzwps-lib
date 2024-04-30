package com.xyzwps.lib.express.server.simple;

import com.sun.net.httpserver.HttpServer;
import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.lib.express.Next;
import com.xyzwps.lib.express.Server;
import com.xyzwps.lib.express.util.Middleware2Composer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public final class SimpleServer implements Server {

    private HttpServer httpServer;

    private final List<Consumer<HttpServer>> serverConfigs = new LinkedList<>();

    private HttpMiddleware middleware = HttpMiddleware.DO_NOTHING;

    private int port = 3000;

    public SimpleServer() {
        serverConfigs.add(hs -> hs.createContext("/", (exchange) -> {
            // TODO: ab -n 20000 -c 10 http://127.0.0.1:3000/debug 在请求超过 14000 个之后会突然变慢
            var req = new SimpleHttpRequest(exchange);
            var resp = new SimpleHttpResponse(exchange);
            this.middleware.call(req, resp, Next.EMPTY);
        }));
    }

    @Override
    public Server use(HttpMiddleware mw) {
        this.middleware = Middleware2Composer.compose2(middleware, mw)::call;
        return this;
    }

    @Override
    public Server listen(int port) {
        this.port = port;
        return this;
    }

    @Override
    public void start() {
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        this.serverConfigs.forEach(config -> config.accept(this.httpServer));
        this.httpServer.start();
    }
}
