package com.xyzwps.website.filter;

import com.xyzwps.lib.express.*;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class LogRequestCostFilter implements Filter {

    private static final Logger log = Logger.getLogger(LogRequestCostFilter.class);

    private static final AtomicLong COUNTER = new AtomicLong(0);

    @Override
    public void filter(HttpRequest req, HttpResponse resp, Next next) {
        log.infof("-> %s %s", req.method(), req.path());
        var t = Thread.currentThread();
        log.infof(" > Thread id:%d name:%s virtual:%s", t.threadId(), t.getName(), t.isVirtual());
        long startTs = System.currentTimeMillis();
        next.next(req, resp);
        log.infof(" > [%d] [%d] %s %s cost %dms ", t.threadId(), COUNTER.getAndIncrement(), req.method(), req.path(), System.currentTimeMillis() - startTs);
    }
}
