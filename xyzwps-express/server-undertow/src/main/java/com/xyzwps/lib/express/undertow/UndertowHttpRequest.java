package com.xyzwps.lib.express.undertow;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpSearchParams;
import io.undertow.server.HttpServerExchange;
import lib.jsdom.mimetype.MimeType;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class UndertowHttpRequest implements HttpRequest {

    private final HttpServerExchange exchange;

    private final HttpMethod method;

    private final HttpSearchParams searchParams;

    private final Map<String, Object> attributes;

    private Object body;

    UndertowHttpRequest(HttpServerExchange exchange) {
        this.exchange = Args.notNull(exchange, "Exchange cannot be null. Maybe a bug.");
        this.method = HttpMethod.valueOf(exchange.getRequestMethod().toString()); // TODO: 处理错误
        this.searchParams = HttpSearchParams.parse(exchange.getQueryString());
        this.attributes = new TreeMap<>();

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
        var headerValue = exchange.getRequestHeaders()
                .get(Args.notNull(name, "Header name cannot be null"));
        if (headerValue == null) {
            return null;
        }

        return headerValue.stream().findFirst().orElse(null);
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
    public Map<String, Object> attributes() {
        return attributes;
    }

    @Override
    public Object attribute(String name) {
        Args.notNull(name, "Name cannot be null");

        return attributes.get(name);
    }

    @Override
    public void attribute(String name, Object value) {
        Args.notNull(name, "Name cannot be null");

        attributes.put(name, value);
    }

    @Override
    public HttpSearchParams searchParams() {
        return searchParams;
    }
}
