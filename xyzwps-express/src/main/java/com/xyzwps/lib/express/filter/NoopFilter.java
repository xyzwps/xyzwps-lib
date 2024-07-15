package com.xyzwps.lib.express.filter;

import com.xyzwps.lib.express.Filter;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;

public enum NoopFilter implements Filter {
    INSTANCE;

    @Override
    public void filter(HttpRequest request, HttpResponse response, Next next) {
        next.next(request, response);
    }
}
