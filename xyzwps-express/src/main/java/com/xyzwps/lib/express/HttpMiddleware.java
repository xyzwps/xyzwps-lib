package com.xyzwps.lib.express;

import com.xyzwps.lib.express.common.Middleware2;
import com.xyzwps.lib.express.common.Middleware2Composer;

import java.util.List;

import static com.xyzwps.lib.dollar.Dollar.*;

public interface HttpMiddleware extends Middleware2<HttpRequest, HttpResponse> {

    HttpMiddleware DO_NOTHING = (req, resp, next) -> next.call();

    static HttpMiddleware compose(List<HttpMiddleware> mws) {
        if ($.isEmpty(mws)) {
            throw new IllegalArgumentException("Nothing to compose");
        }

        Middleware2<HttpRequest, HttpResponse>[] arr = mws.toArray(new HttpMiddleware[0]);
        var composed = Middleware2Composer.compose(arr);
        return composed::call;
    }
}
