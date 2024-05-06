package com.xyzwps.lib.express;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.util.Middleware;

import java.util.Map;
import java.util.TreeMap;

public final class HttpContext implements Middleware.Context<HttpContext> {

    private final HttpMiddleware mw;

    private final HttpContext ctx;

    private final ContextInternal internal;

    HttpContext(HttpMiddleware mw, HttpContext ctx) {
        this.mw = Args.notNull(mw, "mw cannot be null");
        this.ctx = Args.notNull(ctx, "ctx cannot be null");
        this.internal = ctx.internal;
    }

    HttpContext(HttpRequest request, HttpResponse response) {
        this.mw = null;
        this.ctx = null;
        this.internal = new ContextInternal(
                Args.notNull(request, "request cannot be null"),
                Args.notNull(response, "response cannot be null"),
                new TreeMap<>()
        );
    }

    public static HttpContext start(HttpRequest request, HttpResponse response) {
        return new HttpContext(request, response);
    }

    public HttpRequest request() {
        return internal.request();
    }

    public HttpResponse response() {
        return internal.response();
    }

    public Map<String, Object> attributes() {
        return internal.attributes;
    }

    public Object attribute(String name) {
        Args.notNull(name, "Name cannot be null");

        return internal.attributes.get(name);
    }

    public void attribute(String name, Object value) {
        Args.notNull(name, "Name cannot be null");

        internal.attributes.put(name, value);
    }

    @Override
    public void next() {
        if (mw != null) mw.call(ctx);
    }

    private record ContextInternal(HttpRequest request, HttpResponse response, Map<String, Object> attributes) {
    }
}
