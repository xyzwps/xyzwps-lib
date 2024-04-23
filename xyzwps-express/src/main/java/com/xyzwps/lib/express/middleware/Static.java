package com.xyzwps.lib.express.middleware;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.lib.express.HttpStatus;

import java.util.Objects;

public final class Static {

    private final String root;

    public Static(String root) {
        this.root = Objects.requireNonNull(root);
    }

    public HttpMiddleware serve() {
        return (req, resp, next) -> {
            if (req.method() != HttpMethod.GET) {
                next.call();
                return;
            }

            resp.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "text/plain").send((req.path() + '\n').getBytes());
        };
    }
}
