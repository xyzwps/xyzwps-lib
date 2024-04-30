package com.xyzwps.lib.express.server.simple;

import com.sun.net.httpserver.HttpExchange;
import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.HttpStatus;

import java.io.IOException;
import java.io.UncheckedIOException;

class SimpleHttpResponse implements HttpResponse {

    private final HttpExchange exchange;

    private HttpStatus status = HttpStatus.OK;

    SimpleHttpResponse(HttpExchange exchange) {
        this.exchange = Args.notNull(exchange, "HttpExchange cannot be null");
    }

    @Override
    public HttpResponse status(HttpStatus status) {
        this.status = Args.notNull(status, "HttpStatus cannot be null");
        return this;
    }

    @Override
    public HttpResponse header(String name, String value) {
        this.exchange.getResponseHeaders().add(name, value);
        return this;
    }

    @Override
    public void send(byte[] bytes) {
        try {
            this.exchange.sendResponseHeaders(status.code, bytes.length);
            try (var out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
