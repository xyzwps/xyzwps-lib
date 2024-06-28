package com.xyzwps.lib.express.server.helidon;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.*;
import com.xyzwps.lib.express.server.commons.SimpleCookie;
import io.helidon.webserver.http.ServerRequest;
import lib.jsdom.mimetype.MimeType;

import java.io.InputStream;
import java.util.List;

class HelidonHttpRequest implements HttpRequest {

    private Object body;
    private final ServerRequest req;
    private final HttpMethod method;
    private final HttpHeaders headers;
    private final MimeType contentType;
    private final HttpSearchParams searchParams;
    private final HttpPathVariables pathVariables;
    private final Cookies cookies;

    HelidonHttpRequest(ServerRequest request, InputStream in) {
        this.body = in;
        this.req = request;
        this.method = switch (request.prologue().method().text()) {
            case "GET" -> HttpMethod.GET;
            case "POST" -> HttpMethod.POST;
            case "PUT" -> HttpMethod.PUT;
            case "DELETE" -> HttpMethod.DELETE;
            case "PATCH" -> HttpMethod.PATCH;
            case "HEAD" -> HttpMethod.HEAD;
            case "OPTIONS" -> HttpMethod.OPTIONS;
            case "TRACE" -> HttpMethod.TRACE;
            case "CONNECT" -> HttpMethod.CONNECT;
            default -> throw new IllegalArgumentException("Unsupported HTTP method");
        };
        this.headers = HelidonHelper.createHttpHeader(request.headers());

        var type = headers.get(HttpHeaders.CONTENT_TYPE);
        this.contentType = type == null ? null : MimeType.parse(type);

        this.searchParams = HttpSearchParams.parse(req.query().rawValue());
        this.pathVariables = new HttpPathVariables();


        this.cookies = SimpleCookie.from(headers.get(HttpHeaders.COOKIE));
    }

    @Override
    public HttpMethod method() {
        return method;
    }

    @Override
    public String path() {
        return req.path().rawPathNoParams();
    }

    @Override
    public HttpProtocol protocol() {
        return HttpProtocol.valueOf(req.prologue().protocol());
    }

    @Override
    public String header(String name) {
        return headers.get(Args.notNull(name, "Header name cannot be null"));
    }

    @Override
    public List<String> headers(String name) {
        return headers.getAll(Args.notNull(name, "Header name cannot be null"));
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }

    @Override
    public MimeType contentType() {
        return contentType;
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
    public Cookies cookies() {
        return cookies;
    }
}
