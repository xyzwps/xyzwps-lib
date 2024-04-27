package com.xyzwps.lib.express.undertow;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.HttpStatus;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.nio.ByteBuffer;

class UndertowHttpResponse implements HttpResponse {

    private final HttpServerExchange exchange;

    UndertowHttpResponse(HttpServerExchange exchange) {
        this.exchange = Args.notNull(exchange, "Exchange cannot be null. Maybe a bug.");
    }

    @Override
    public HttpResponse status(HttpStatus status) {
        exchange.setStatusCode(status.code);
        return this;
    }

    @Override
    public HttpResponse header(String name, String value) {
        exchange.getResponseHeaders().put(new HttpString(name), value);
        return this;
    }

    @Override
    public void send(byte[] bytes) {
        exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
    }
}
