package com.xyzwps.lib.express.server.undertow;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.HttpStatus;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.nio.ByteBuffer;

class UndertowHttpResponse implements HttpResponse {

    private final HttpServerExchange exchange;
    private final HttpHeaders headers;

    UndertowHttpResponse(HttpServerExchange exchange) {
        this.exchange = Args.notNull(exchange, "Exchange cannot be null. Maybe a bug.");
        this.headers = new HttpHeaders();
    }

    @Override
    public void status(HttpStatus status) {
        exchange.setStatusCode(status.code);
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }

    @Override
    public void send(byte[] bytes) {
        var exheaders = this.exchange.getResponseHeaders();
        for (var name : headers.names()) {
            exheaders.addAll(new HttpString(name), headers.getAll(name));
        }

        exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
    }
}
