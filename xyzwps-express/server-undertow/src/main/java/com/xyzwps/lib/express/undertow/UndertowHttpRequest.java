package com.xyzwps.lib.express.undertow;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpSearchParams;
import io.undertow.server.HttpServerExchange;
import lib.jsdom.mimetype.MimeType;

import java.util.List;

class UndertowHttpRequest implements HttpRequest {

    private final HttpServerExchange exchange;

    private final HttpMethod method;

    private final HttpSearchParams searchParams;

    private Object body;

    UndertowHttpRequest(HttpServerExchange exchange) {
        this.exchange = Args.notNull(exchange, "Exchange cannot be null. Maybe a bug.");
        this.method = HttpMethod.valueOf(exchange.getRequestMethod().toString()); // TODO: 处理错误
        this.searchParams = HttpSearchParams.parse(exchange.getQueryString());

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
    public HttpHeaders headers() {
        var headers = new HttpHeaders();
        var rawHeaders = exchange.getRequestHeaders();
        for (var name : rawHeaders.getHeaderNames()) {
            var values = rawHeaders.get(name);
            if (values == null) {
                continue;
            }
            for (var value : values) {
                headers.append(name.toString(), value);
            }
        }
        return headers;
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

    @Override
    public HttpSearchParams searchParams() {
        return searchParams;
    }
}
