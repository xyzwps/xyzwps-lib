package com.xyzwps.website.middleware;

import com.xyzwps.lib.express.*;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class LogRequestCostMiddleware implements HttpMiddleware {

    private static final Logger log = Logger.getLogger(LogRequestCostMiddleware.class);

    private static final AtomicLong COUNTER = new AtomicLong(0);

    @Override
    public void call(HttpContext ctx) {
        var req = ctx.request();
        log.infof("-> %s %s", req.method(), req.path());
        var t = Thread.currentThread();
        log.infof(" > Thread id:%d name:%s virtual:%s", t.threadId(), t.getName(), t.isVirtual());
        long startTs = System.currentTimeMillis();
        ctx.next();
        log.infof(" > [%d] [%d] %s %s cost %dms ", t.threadId(), COUNTER.getAndIncrement(), req.method(), req.path(), System.currentTimeMillis() - startTs);
    }
}
