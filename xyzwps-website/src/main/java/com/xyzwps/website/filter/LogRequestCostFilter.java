package com.xyzwps.website.filter;

import com.xyzwps.lib.express.*;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class LogRequestCostFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LogRequestCostFilter.class);

    private static final AtomicLong COUNTER = new AtomicLong(0);

    @Override
    public void filter(HttpRequest req, HttpResponse resp, Next next) {
        log.info("-> {} {}", req.method(), req.path());
        var t = Thread.currentThread();
        log.info(" > Thread id:{} name:{} virtual:{}", t.threadId(), t.getName(), t.isVirtual());
        long startTs = System.currentTimeMillis();
        next.next(req, resp);
        log.info(" > [{}] [{}] {} {} cost {}ms ", t.threadId(), COUNTER.getAndIncrement(), req.method(), req.path(), System.currentTimeMillis() - startTs);
    }
}
