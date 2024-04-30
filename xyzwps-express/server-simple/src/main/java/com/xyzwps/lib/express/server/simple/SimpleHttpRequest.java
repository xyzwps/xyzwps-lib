package com.xyzwps.lib.express.server.simple;

import com.sun.net.httpserver.HttpExchange;
import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpSearchParams;
import lib.jsdom.mimetype.MimeType;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class SimpleHttpRequest implements HttpRequest {

    private final HttpExchange exchange;
    private final Map<String, Object> attributes;

    private Object body;

    SimpleHttpRequest(HttpExchange exchange) {
        this.exchange = Args.notNull(exchange, "HttpExchange cannot be null");
        this.attributes = new TreeMap<>();

        this.body = exchange.getRequestBody();
    }

    @Override
    public HttpMethod method() {
        return HttpMethod.from(exchange.getRequestMethod());
    }

    @Override
    public String path() {
        return exchange.getRequestURI().getPath();
    }

    @Override
    public String protocol() {
        return exchange.getProtocol();
    }

    @Override
    public String header(String name) {
        return exchange.getRequestHeaders().getFirst(name);
    }

    @Override
    public List<String> headers(String name) {
        return exchange.getRequestHeaders().get(name);
    }

    @Override
    public HttpHeaders headers() {
        return null; // TODO: 搞一下
    }

    @Override
    public MimeType contentType() {
        return null; // TODO: 搞一下
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
        return null;  // TODO: 搞一下
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
}
