package com.xyzwps.lib.express;

import java.util.Objects;
import java.util.Optional;

public class HttpRequest {
    private final HttpMethod method;
    private final String url;
    private final String protocol;
    private final HttpHeaders headers;
    private Object body;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<MimeType> contentType;

    public HttpRequest(HttpMethod method, String url, String protocol, HttpHeaders headers, Object body) {
        this.method = Objects.requireNonNull(method);
        this.url = Objects.requireNonNull(url);
        this.protocol = Objects.requireNonNull(protocol);
        this.headers = Objects.requireNonNull(headers);
        this.body = body;
        this.contentType = headers.contentType().map(MimeType::parse);
    }

    public HttpMethod method() {
        return method;
    }

    public String url() {
        return url;
    }

    public String protocol() {
        return protocol;
    }

    public Optional<String> header(String name) {
        return headers.getFirst(name);
    }

    public Object body() {
        return body;
    }

    public void body(Object body) {
        this.body = body;
    }

    public Optional<MimeType> contentType() {
        return contentType;
    }
}
