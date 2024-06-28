package com.xyzwps.lib.express.server.bio;

import com.xyzwps.lib.express.*;
import com.xyzwps.lib.express.server.commons.SimpleCookie;
import lib.jsdom.mimetype.MimeType;

import java.net.URI;
import java.util.List;
import java.util.Objects;

public final class BioHttpRequest implements HttpRequest {
    private final HttpMethod method;
    private final String path;
    private final HttpProtocol protocol;
    private final HttpHeaders headers;
    private final HttpSearchParams searchParams;
    private final HttpPathVariables pathVariables;
    private final Cookies cookies;
    private Object body;

    private final MimeType contentType;

    public BioHttpRequest(HttpMethod method, URI uri, HttpProtocol protocol, HttpHeaders headers, Object body) {
        this.path = Objects.requireNonNull(uri).getPath();
        this.searchParams = HttpSearchParams.parse(uri.getRawQuery());

        this.method = Objects.requireNonNull(method);
        this.protocol = Objects.requireNonNull(protocol);
        this.headers = Objects.requireNonNull(headers);
        this.body = body;

        var contentTypeStr = headers.contentType();
        this.contentType = contentTypeStr == null ? null : MimeType.parse(contentTypeStr);
        this.pathVariables = new HttpPathVariables();

        this.cookies = SimpleCookie.from(HttpHeaders.COOKIE);
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
    public HttpProtocol protocol() {
        return protocol;
    }

    @Override
    public String header(String name) {
        return headers.get(name);
    }

    @Override
    public List<String> headers(String name) {
        return headers.getAll(name);
    }

    @Override
    public HttpHeaders headers() {
        return headers;
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

    @Override
    public HttpPathVariables pathVariables() {
        return pathVariables;
    }

    @Override
    public MimeType contentType() {
        return contentType;
    }

    @Override
    public Cookies cookies() {
        return cookies;
    }
}
