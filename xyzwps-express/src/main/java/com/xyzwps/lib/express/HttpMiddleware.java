package com.xyzwps.lib.express;

import com.xyzwps.lib.express.common.Middleware2;

public interface HttpMiddleware extends Middleware2<HttpRequest<?>, HttpResponse> {

    HttpMiddleware DO_NOTHING = (req, resp, next) -> next.call();
}
