package com.xyzwps.lib.express.server;

import com.xyzwps.lib.express.core.HttpMethod;
import com.xyzwps.lib.express.core.HttpRequest;
import lib.jsdom.mimetype.MimeType;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public final class SimpleHttpRequest implements HttpRequest {
    private final HttpMethod method;
    private final String path;
    private final String protocol;
    private final HttpHeaders headers;
    private final HttpSearchParams searchParams;
    private Object body;

    // TODO: attribute

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<MimeType> contentType;

    public SimpleHttpRequest(HttpMethod method, URI uri, String protocol, HttpHeaders headers, Object body) {
        this.path = Objects.requireNonNull(uri).getPath();
        this.searchParams = HttpSearchParams.parse(uri.getRawQuery());

        this.method = Objects.requireNonNull(method);
        this.protocol = Objects.requireNonNull(protocol);
        this.headers = Objects.requireNonNull(headers);
        this.body = body;
        this.contentType = headers.contentType().map(MimeType::parse);
    }

    @Override
    public HttpMethod method() {
        return method;
    }

    @Override
    public String path() {
        return path;
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
    public Object body() {
        return body;
    }

    @Override
    public void body(Object body) {
        this.body = body;
    }

    @Override
    public Optional<MimeType> contentType() {
        return contentType;
    }
}
