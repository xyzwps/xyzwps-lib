package com.xyzwps.website.middleware;

import com.xyzwps.lib.express.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class LogRequestCostMiddleware implements HttpMiddleware {

    private static final Logger log = LoggerFactory.getLogger(LogRequestCostMiddleware.class);

    private static final AtomicLong COUNTER = new AtomicLong(0);

    @Inject
    LogRequestCostMiddleware() {
    }

    @Override
    public void call(HttpContext ctx) {
        var req = ctx.request();
        log.info("-> {} {}", req.method(), req.path());
        var t = Thread.currentThread();
        log.info(" > Thread id:{} name:{} virtual:{}", t.threadId(), t.getName(), t.isVirtual());
        long startTs = System.currentTimeMillis();
        ctx.next();
        log.info(" > [{}] [{}] {} {} cost {}ms ", t.threadId(), COUNTER.getAndIncrement(), req.method(), req.path(), System.currentTimeMillis() - startTs);
    }
}
