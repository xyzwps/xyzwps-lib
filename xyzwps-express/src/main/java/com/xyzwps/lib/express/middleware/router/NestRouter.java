package com.xyzwps.lib.express.middleware.router;

import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpMiddleware;

import java.util.function.Consumer;

public class NestRouter extends Router<NestRouter> {

    NestRouter(int matchStart) {
        super(matchStart);
    }

    @Override
    public NestRouter handle(HttpMethod method, String url, HttpMiddleware mw0, HttpMiddleware... mws) {
        handle0(method, url, mw0, mws);
        return this;
    }

    @Override
    public NestRouter use(HttpMiddleware mw) {
        use0(mw);
        return this;
    }

    @Override
    public NestRouter nest(String prefix, Consumer<NestRouter> nestRouterBuilder) {
        nest0(prefix, nestRouterBuilder);
        return this;
    }

}
