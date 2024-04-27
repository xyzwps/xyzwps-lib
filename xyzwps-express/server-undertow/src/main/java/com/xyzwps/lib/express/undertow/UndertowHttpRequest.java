package com.xyzwps.lib.express.undertow;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpRequest;
import io.undertow.server.HttpServerExchange;
import lib.jsdom.mimetype.MimeType;

import java.util.List;

class UndertowHttpRequest implements HttpRequest {

    private final HttpServerExchange exchange;

    private final HttpMethod method;

    private Object body;

    UndertowHttpRequest(HttpServerExchange exchange) {
        this.exchange = Args.notNull(exchange, "Exchange cannot be null. Maybe a bug.");
        this.method = HttpMethod.valueOf(exchange.getRequestMethod().toString()); // TODO: 处理错误
        exchange.startBlocking(); // TODO: wrap start blocking with lazy input stream
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
    public String header(String name) {
        return exchange.getRequestHeaders()
                .get(Args.notNull(name, "Header name cannot be null"))
                .stream().findFirst().orElse(null);
    }

    @Override
    public List<String> headers(String name) {
        return List.copyOf(exchange.getRequestHeaders()
                .get(Args.notNull(name, "Header name cannot be null")));
    }

    @Override
    public MimeType contentType() {
        return exchange.getRequestHeaders().get("Content-Type")
                .stream().findFirst().map(MimeType::parse).orElse(null);
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
