package com.xyzwps.lib.express.server.undertow;

import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.lib.express.util.Middleware2Composer;
import com.xyzwps.lib.express.Next;
import com.xyzwps.lib.express.Server;
import io.undertow.Undertow;

import java.util.concurrent.locks.ReentrantLock;

public final class UndertowServer implements Server {

    private HttpMiddleware middleware = HttpMiddleware.DO_NOTHING;

    private boolean running = false;

    private boolean portSet = false;

    private final ReentrantLock lock = new ReentrantLock();

    private final Undertow.Builder builder = Undertow.builder();

    @Override
    public Server use(HttpMiddleware mw) {
        return initWithLock(() -> this.middleware = Middleware2Composer.compose2(middleware, mw)::call);
    }

    private <T> Server initWithLock(Runnable run) {
        if (lock.tryLock()) {
            try {
                run.run();
            } finally {
                lock.unlock();
            }
            return this;
        } else {
            throw new IllegalStateException("You cannot init server concurrently");
        }
    }

    @Override
    public Server listen(int port) {
        return initWithLock(() -> {
            if (portSet) {
                throw new IllegalStateException("You cannot init server concurrently");
            }
            this.builder.addHttpListener(port, "localhost");
            this.portSet = true;
        });
    }

    @Override
    public void start() {
        initWithLock(() -> {
            if (this.running) {
                throw new IllegalStateException("Server is running");
            } else {
                this.running = true;
            }
        });

        this.builder
                .setHandler((exchange -> {
                    // TODO: ab -n 20000 -c 10 http://127.0.0.1:3000/debug 在请求超过 14000 个之后会突然变慢
                    try (var ignored = exchange.startBlocking()) {
                        try (var in = exchange.getInputStream()) {
                            var req = new UndertowHttpRequest(exchange, in);
                            var resp = new UndertowHttpResponse(exchange);
                            this.middleware.call(req, resp, Next.EMPTY);
                        }
                    }
                }))
                .build()
                .start();
    }
}
