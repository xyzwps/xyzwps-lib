package com.xyzwps.lib.express.server.undertow;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpContext;
import com.xyzwps.lib.express.ServerConfig;
import com.xyzwps.lib.express.Server;
import io.undertow.Undertow;

import java.io.IOException;
import java.io.UncheckedIOException;

public final class UndertowServer implements Server {

    @Override
    public void start(ServerConfig config) {
        Args.notNull(config, "ServerConfig cannot be null");

        Undertow.builder()
                .addHttpListener(config.port, "localhost")
                .setHandler((exchange -> {
                    // TODO: 怎么跑到 virtual thread 上？
                    try (var ignored = exchange.startBlocking()) {
                        try (var in = exchange.getInputStream()) {
                            var req = new UndertowHttpRequest(exchange, in);
                            var resp = new UndertowHttpResponse(exchange);
                            config.middleware.call(HttpContext.start(req, resp));
                        }
                    } catch (IOException e) {
                        // TODO: 处理错误
                        throw new UncheckedIOException(e);
                    }
                }))
                .build()
                .start();
    }
}
