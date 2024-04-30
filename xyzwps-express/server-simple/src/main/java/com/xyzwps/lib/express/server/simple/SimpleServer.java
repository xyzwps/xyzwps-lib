package com.xyzwps.lib.express.server.simple;

import com.sun.net.httpserver.HttpServer;
import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.Next;
import com.xyzwps.lib.express.Server;
import com.xyzwps.lib.express.ServerConfig;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;

public final class SimpleServer implements Server {

    @Override
    public void start(ServerConfig config) {
        Args.notNull(config, "ServerConfig cannot be null");

        HttpServer httpServer;

        try {
            httpServer = HttpServer.create(new InetSocketAddress(config.port), 0);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        httpServer.createContext("/", (exchange) -> {
            // TODO: ab -n 20000 -c 10 http://127.0.0.1:3000/debug 在请求超过 14000 个之后会突然变慢
            var req = new SimpleHttpRequest(exchange);
            var resp = new SimpleHttpResponse(exchange);
            config.middleware.call(req, resp, Next.EMPTY);
        });
        httpServer.start();
    }
}
