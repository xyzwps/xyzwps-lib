package com.xyzwps.lib.express.server.undertow;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.*;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import lib.jsdom.mimetype.MimeType;
import org.jboss.logging.Logger;

import java.io.InputStream;
import java.net.HttpCookie;
import java.util.List;

import static com.xyzwps.lib.dollar.Dollar.*;

class UndertowHttpRequest implements HttpRequest {

    private static final Logger log = Logger.getLogger(UndertowHttpRequest.class);

    private final HttpServerExchange exchange;

    private final HttpMethod method;

    private final HttpSearchParams searchParams;

    private final HttpHeaders headers;

    private final HttpProtocol protocol;

    private final HttpPathVariables pathVariables;

    private final List<Cookie> cookies;

    private Object body;

    UndertowHttpRequest(HttpServerExchange exchange, InputStream in) {
        this.exchange = Args.notNull(exchange, "Exchange cannot be null. Maybe a bug.");
        this.method = HttpMethod.valueOf(exchange.getRequestMethod().toString()); // TODO: 处理错误
        this.searchParams = HttpSearchParams.parse(exchange.getQueryString());
        this.headers = new UndertowHttpHeaders(exchange.getRequestHeaders());
        this.protocol = HttpProtocol.from(exchange.getProtocol().toString())
                .peekLeft(log::errorf)
                .rightOrThrow(BadProtocolException::new);
        this.body = in;
        this.pathVariables = new HttpPathVariables();
        this.cookies = $.arrayListFrom(exchange.requestCookies().iterator());
    }

    @Override
    public HttpMethod method() {
        return method;
    }

    @Override
    public String path() {
        return exchange.getRequestPath();
    }

    @Override
    public HttpProtocol protocol() {
        return protocol;
    }

    @Override
    public String header(String name) {
        var headerValue = exchange.getRequestHeaders()
                .get(Args.notNull(name, "Header name cannot be null"));
        if (headerValue == null) {
            return null;
        }

        return headerValue.stream().findFirst().orElse(null);
    }

    @Override
    public List<String> headers(String name) {
        return List.copyOf(exchange.getRequestHeaders()
                .get(Args.notNull(name, "Header name cannot be null")));
    }

    @Override
    public HttpHeaders headers() {
        return this.headers;
    }

    @Override
    public MimeType contentType() {
        return exchange.getRequestHeaders().get("Content-Type")
                .stream().findFirst().map(MimeType::parse).orElse(null);
        // TODO: http headers
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
    public HttpCookie cookie(String name) {
        if (cookies.isEmpty()) {
            return null;
        }

        return cookies.stream().findFirst().map(c -> {
            var cookie = new HttpCookie(c.getName(), c.getValue());
            cookie.setDomain(c.getDomain());
            cookie.setPath(c.getPath());
            cookie.setMaxAge(c.getMaxAge());
            cookie.setSecure(c.isSecure());
            cookie.setVersion(c.getVersion());
            cookie.setHttpOnly(c.isHttpOnly());
            cookie.setDiscard(c.isDiscard());
            cookie.setComment(c.getComment());
            return cookie;
        }).orElse(null);
    }
}
