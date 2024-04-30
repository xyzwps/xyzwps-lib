package com.xyzwps.lib.express.server.craft;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpSearchParams;
import lib.jsdom.mimetype.MimeType;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public final class CraftHttpRequest implements HttpRequest {
    private final HttpMethod method;
    private final String path;
    private final String protocol;
    private final HttpHeaders headers;
    private final HttpSearchParams searchParams;
    private final Map<String, Object> attributes;
    private Object body;

    private final MimeType contentType;

    public CraftHttpRequest(HttpMethod method, URI uri, String protocol, HttpHeaders headers, Object body) {
        this.path = Objects.requireNonNull(uri).getPath();
        this.searchParams = HttpSearchParams.parse(uri.getRawQuery());

        this.method = Objects.requireNonNull(method);
        this.protocol = Objects.requireNonNull(protocol);
        this.headers = Objects.requireNonNull(headers);
        this.body = body;
        this.attributes = new TreeMap<>();

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
    public MimeType contentType() {
        return contentType;
    }



}