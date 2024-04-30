package com.xyzwps.lib.express.server.undertow;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.ServerConfig;
import com.xyzwps.lib.express.Next;
import com.xyzwps.lib.express.Server;
import io.undertow.Undertow;

public final class UndertowServer implements Server {

    @Override
    public void start(ServerConfig config) {
        Args.notNull(config, "ServerConfig cannot be null");

        Undertow.builder()
                .addHttpListener(config.port, "localhost")
                .setHandler((exchange -> {
                    // TODO: ab -n 20000 -c 10 http://127.0.0.1:3000/debug 在请求超过 14000 个之后会突然变慢
                    try (var ignored = exchange.startBlocking()) {
                        try (var in = exchange.getInputStream()) {
                            var req = new UndertowHttpRequest(exchange, in);
                            var resp = new UndertowHttpResponse(exchange);
                            config.middleware.call(req, resp, Next.EMPTY);
                        }
                    }
                }))
                .build()
                .start();
    }
}
