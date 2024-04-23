package com.xyzwps.lib.express;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public final class HttpRequest {
    private final HttpMethod method;
    private final String path;
    private final String protocol;
    private final HttpHeaders headers;
    private final HttpSearchParams searchParams;
    private Object body;

    // TODO: attribute

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<MimeType> contentType;

    public HttpRequest(HttpMethod method, URI uri, String protocol, HttpHeaders headers, Object body) {
        this.path = Objects.requireNonNull(uri).getPath();
        this.searchParams = HttpSearchParams.parse(uri.getRawQuery());

        this.method = Objects.requireNonNull(method);
        this.protocol = Objects.requireNonNull(protocol);
        this.headers = Objects.requireNonNull(headers);
        this.body = body;
        this.contentType = headers.contentType().map(MimeType::parse);
    }

    public HttpMethod method() {
        return method;
    }

    public String path() {
        return path;
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
