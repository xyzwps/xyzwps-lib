package com.xyzwps.lib.express.undertow;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.core.HttpMethod;
import com.xyzwps.lib.express.core.HttpRequest;
import io.undertow.server.HttpServerExchange;
import lib.jsdom.mimetype.MimeType;

import java.util.Optional;

class UndertowHttpRequest implements HttpRequest {

    private final HttpServerExchange exchange;

    private final HttpMethod method;

    private Object body;

    UndertowHttpRequest(HttpServerExchange exchange) {
        this.exchange = Args.notNull(exchange, "Exchange cannot be null. Maybe a bug.");
        this.method = HttpMethod.valueOf(exchange.getRequestMethod().toString()); // TODO: 处理错误
        exchange.startBlocking();
        this.body = exchange.getInputStream();
    }

    @Override
    public HttpMethod method() {
        return method;
    }

    @Override
    public String path() {
        return exchange.getRequestPath();
    }

    @Override
    public String protocol() {
        return exchange.getProtocol().toString(); // TODO: 测试
    }

    @Override
    public Optional<String> header(String name) {
        return exchange.getRequestHeaders().get(name).stream().findFirst();
    }

    @Override
    public Optional<MimeType> contentType() {
        return exchange.getRequestHeaders().get("Content-Type").stream().findFirst().map(MimeType::parse);
        // TODO: http headers
    }

    @Override
    public Object body() {
        return body;
    }

    @Override
    public void body(Object body) {
        this.body = body;
    }
}
