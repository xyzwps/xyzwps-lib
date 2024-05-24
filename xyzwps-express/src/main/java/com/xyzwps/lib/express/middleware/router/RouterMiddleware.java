package com.xyzwps.lib.express.middleware.router;

import com.xyzwps.lib.express.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.xyzwps.lib.dollar.Dollar.$;

public class RouterMiddleware extends Router<RouterMiddleware> implements HttpMiddleware {

    private final Consumer<HttpContext> _404Handler;

    public RouterMiddleware(Consumer<HttpContext> _404Handler) {
        super(0);
        this._404Handler = $.defaultTo(_404Handler, RouterMiddleware::handle404);
    }

    public RouterMiddleware() {
        this(null);
    }

    @Override
    public void call(HttpContext context) {
        var req = context.request();
        var path = HPath.pathToSegmentStrings(req.path());
        var mws = this.match(req, path);
        if (mws.isEmpty()) {
            this._404Handler.accept(context);
        } else {
            HttpMiddleware.compose(mws).call(context);
        }
    }

    @Override
    public RouterMiddleware handle(HttpMethod method, String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        handle0(method, url, mw0, mws);
        return this;
    }

    @Override
    public RouterMiddleware use(HttpMiddleware mw) {
        use0(mw);
        return this;
    }

    @Override
    public RouterMiddleware nest(String prefix, Consumer<NestRouter> nestRouterBuilder) {
        nest0(prefix, nestRouterBuilder);
        return this;
    }

    private List<HttpMiddleware> match(HttpRequest request, String[] path) {
        List<HttpMiddleware> mws = new ArrayList<>(8);
        match(request, path, this, mws);
        return mws;
    }

    private static void match(HttpRequest request, String[] path, Router<?> router, List<HttpMiddleware> mws) {
        var method = request.method();
        var pathVariables = request.pathVariables();

        for (var item : router.items) {
            switch (item) {
                case RouteItem.Handler handler -> {
                    if (handler.method() == null || handler.method() == method) {
                        if (handler.url().match(path, router.matchStart)) {
                            handler.url().pathVars(path, router.matchStart).forEach(p -> pathVariables.add(p.key(), p.value()));
                            mws.addAll(Arrays.asList(handler.middlewares()));
                            return;
                        }
                    }
                }
                case RouteItem.Use use -> mws.add(use.mw());
                case RouteItem.Nest nest -> {
                    if (nest.prefix().isPrefixOf(path, router.matchStart)) {
                        nest.prefix().pathVars(path, router.matchStart).forEach(p -> pathVariables.add(p.key(), p.value()));
                        match(request, path, nest.router(), mws);
                        return;
                    }
                }
            }
        }
    }

    private static void handle404(HttpContext ctx) {
        var resp = ctx.response();
        resp.headers().set(HttpHeaders.CONTENT_TYPE, "text/plain");
        resp.status(HttpStatus.NOT_FOUND);
        resp.send("Not Found".getBytes());
    }
}
