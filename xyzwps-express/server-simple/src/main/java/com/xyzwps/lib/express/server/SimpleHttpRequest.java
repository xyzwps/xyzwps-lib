package com.xyzwps.lib.express.server;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpRequest;
import lib.jsdom.mimetype.MimeType;

import java.net.URI;
import java.util.List;
import java.util.Objects;

public final class SimpleHttpRequest implements HttpRequest {
    private final HttpMethod method;
    private final String path;
    private final String protocol;
    private final HttpHeaders headers;
    private final HttpSearchParams searchParams;
    private Object body;

    // TODO: attribute

    private final MimeType contentType;

    public SimpleHttpRequest(HttpMethod method, URI uri, String protocol, HttpHeaders headers, Object body) {
        this.path = Objects.requireNonNull(uri).getPath();
        this.searchParams = HttpSearchParams.parse(uri.getRawQuery());

        this.method = Objects.requireNonNull(method);
        this.protocol = Objects.requireNonNull(protocol);
        this.headers = Objects.requireNonNull(headers);
        this.body = body;

        var contentTypeStr = headers.contentType();
        this.contentType = contentTypeStr == null ? null : MimeType.parse(contentTypeStr);
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
    public String header(String name) {
        return headers.getFirst(name);
    }

    @Override
    public List<String> headers(String name) {
        return headers.getAll(name);
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
    public MimeType contentType() {
        return contentType;
    }
}
