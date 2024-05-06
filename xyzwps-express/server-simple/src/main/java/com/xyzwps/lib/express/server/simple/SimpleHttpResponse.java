package com.xyzwps.lib.express.server.simple;

import com.sun.net.httpserver.HttpExchange;
import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.HttpStatus;

import java.io.IOException;
import java.io.UncheckedIOException;

class SimpleHttpResponse implements HttpResponse {

    private final HttpExchange exchange;
    private final HttpHeaders headers;

    private HttpStatus status = HttpStatus.OK;

    SimpleHttpResponse(HttpExchange exchange) {
        this.exchange = Args.notNull(exchange, "HttpExchange cannot be null");
        this.headers = new SimpleHttpHeaders(exchange.getResponseHeaders());
    }

    @Override
    public void status(HttpStatus status) {
        this.status = Args.notNull(status, "HttpStatus cannot be null");
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }

    @Override
    public void send(byte[] bytes) {
        try {
            this.exchange.sendResponseHeaders(status.code, bytes.length);

            var exheaders = this.exchange.getResponseHeaders();
            for (var name : headers.names()) {
                exheaders.put(name, headers.getAll(name));
            }

            try (var out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
