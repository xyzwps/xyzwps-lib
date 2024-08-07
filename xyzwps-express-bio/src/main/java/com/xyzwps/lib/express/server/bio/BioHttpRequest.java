package com.xyzwps.lib.express.server.bio;

import com.xyzwps.lib.express.*;
import com.xyzwps.lib.express.commons.SimpleCookie;
import com.xyzwps.lib.http.HttpMethod;
import com.xyzwps.lib.http.MediaType;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class BioHttpRequest implements HttpRequest {
    private final HttpMethod method;
    private final String path;
    private final HttpProtocol protocol;
    private final HttpHeaders headers;
    private final HttpSearchParams searchParams;
    private final HttpPathVariables pathVariables;
    private final Cookies cookies;
    private final Map<String, Object> attributes;
    private Object body;

    private final MediaType contentType;

    public BioHttpRequest(HttpMethod method, URI uri, HttpProtocol protocol, HttpHeaders headers, Object body) {
        this.path = Objects.requireNonNull(uri).getPath();
        this.searchParams = HttpSearchParams.parse(uri.getRawQuery());

        this.method = Objects.requireNonNull(method);
        this.protocol = Objects.requireNonNull(protocol);
        this.headers = Objects.requireNonNull(headers);
        this.body = body;

        var contentTypeStr = headers.contentType();
        this.contentType = contentTypeStr == null ? null : MediaType.parse(contentTypeStr);
        this.pathVariables = new HttpPathVariables();
        this.attributes = new HashMap<>();

        this.cookies = SimpleCookie.from(headers.get(HttpHeaders.COOKIE));
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
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public Cookies cookies() {
        return cookies;
    }

    @Override
    public Object attribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Map<String, Object> attributes() {
        return attributes;
    }

    @Override
    public void attribute(String name, Object value) {
        attributes.put(name, value);
    }
}
