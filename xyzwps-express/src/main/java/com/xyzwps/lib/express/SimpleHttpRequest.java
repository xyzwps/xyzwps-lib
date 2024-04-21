package com.xyzwps.lib.express;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SimpleHttpRequest<BODY> implements HttpRequest<BODY> {
    private final HttpMethod method;
    private final String url;
    private final String protocol;
    private final HttpHeaders headers;
    private final BODY body;

    public SimpleHttpRequest(HttpMethod method, String url, String protocol, HttpHeaders headers, BODY body) {
        this.method = Objects.requireNonNull(method);
        this.url = Objects.requireNonNull(url);
        this.protocol = Objects.requireNonNull(protocol);
        this.headers = Objects.requireNonNull(headers);
        this.body = body;
    }

    @Override
    public HttpMethod method() {
        return method;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public String protocol() {
        return protocol;
    }

    @Override
    public Optional<String> header(String name) {
        return headers.getFirst(name);
    }

    @Override
    public BODY body() {
        return body;
    }
}
