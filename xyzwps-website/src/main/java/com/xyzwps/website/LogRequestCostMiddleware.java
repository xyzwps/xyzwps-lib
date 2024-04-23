package com.xyzwps.website;

import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.common.Next;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LogRequestCostMiddleware implements HttpMiddleware {

    @Inject
    LogRequestCostMiddleware() {
    }

    @Override
    public void call(HttpRequest req, HttpResponse resp, Next next) {
        System.out.printf("-> %s %s \n", req.method(), req.path());
        long startTs = System.currentTimeMillis();
        next.call();
        System.out.printf(" > %s %s cost %dms \n", req.method(), req.path(), System.currentTimeMillis() - startTs);
    }
}
