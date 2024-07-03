package com.xyzwps.lib.express.server.helidon;

import com.xyzwps.lib.express.ServerConfig;
import io.helidon.webserver.http.Handler;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

import java.io.IOException;
import java.io.UncheckedIOException;

record AnyHandler(ServerConfig config) implements Handler {

    @Override
    public void handle(ServerRequest req, ServerResponse res) {
        try (var in = req.content().inputStream()) {
            var request = new HelidonHttpRequest(req, in);
            var response = new HelidonHttpResponse(res);
            config.middleware.call(HttpContext.start(request, response));
        } catch (IOException e) {
            // TODO: 处理错误
            throw new UncheckedIOException(e);
        }
    }
}
