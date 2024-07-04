package com.xyzwps.website.modules.test;

import com.xyzwps.lib.express.Filter;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
public record TestCountFilter(int count) implements Filter {

    private static final String ATTR = "__test_count_filter__";

    @Override
    public void filter(HttpRequest request, HttpResponse response, Next next) {
        var value = request.attribute(ATTR);
        if (value == null) {
            if (count == 1) {
                request.attribute(ATTR, "b1");
            }
        } else if (value instanceof String s) {
            request.attribute(ATTR, s + "b" + count);
        }

        next.next(request, response);

        if (request.attribute(ATTR) instanceof String s) {
            request.attribute(ATTR, s + "a" + count);
        }

        log.infof(" > count: %s", request.attribute(ATTR));
    }
}
