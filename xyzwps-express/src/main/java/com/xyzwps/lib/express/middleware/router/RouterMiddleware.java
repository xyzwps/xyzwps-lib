package com.xyzwps.lib.express.middleware.router;

import com.xyzwps.lib.express.*;

import java.util.function.Consumer;

import static com.xyzwps.lib.dollar.Dollar.$;

public class RouterMiddleware implements HttpMiddleware {

    private final Consumer<HttpContext> _404Handler;

    private final Trie trie;

    public RouterMiddleware(Consumer<HttpContext> _404Handler, Router router) {
        this._404Handler = $.defaultTo(_404Handler, RouterMiddleware::handle404);
        this.trie = new Trie(router);
    }

    public RouterMiddleware(Router router) {
        this(null, router);
    }

    @Override
    public void call(HttpContext context) {
        var req = context.request();
        var mw = this.trie.trie(req.method()).find(req.path());
        if (mw == null) {
            this._404Handler.accept(context);
        } else {
            mw.call(context);
        }
    }


    private static void handle404(HttpContext ctx) {
        var resp = ctx.response();
        resp.headers().set(HttpHeaders.CONTENT_TYPE, "text/plain");
        resp.status(HttpStatus.NOT_FOUND);
        resp.send("Not Found".getBytes());
    }
}
