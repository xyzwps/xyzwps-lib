package com.xyzwps.website.middleware;

import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.common.Next;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class LogRequestCostMiddleware implements HttpMiddleware {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    @Inject
    LogRequestCostMiddleware() {
    }

    @Override
    public void call(HttpRequest req, HttpResponse resp, Next next) {
        System.out.printf("-> %s %s \n", req.method(), req.path());
        long startTs = System.currentTimeMillis();
        next.call();
        System.out.printf(" > [%d] %s %s cost %dms \n", COUNTER.getAndIncrement(), req.method(), req.path(), System.currentTimeMillis() - startTs);
    }
}
