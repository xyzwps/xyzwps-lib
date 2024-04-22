package com.xyzwps.lib.express.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzwps.lib.express.Args;
import com.xyzwps.lib.express.HttpMiddleware;

import java.io.IOException;
import java.io.InputStream;

public final class JsonParser {

    private final ObjectMapper om;

    public JsonParser(ObjectMapper om) {
        this.om = Args.notNull(om, "ObjectMapper cannot be null");
    }

    public <T> HttpMiddleware json(Class<T> tClass) {
        return (req, resp, next) -> {
            var contentType = req.contentType().orElse("");
            // TODO: 解析 media type https://github.com/jsdom/whatwg-mimetype/blob/main/lib/parser.js
            // TODO: https://www.iana.org/assignments/media-types/media-types.xhtml
            if (!contentType.startsWith("application/json")) {
                next.call();
                return;
            }

            if (!(req.body() instanceof InputStream)) {
                next.call();
                return;
            }

            parseBody(tClass).call(req, resp, next);
        };
    }

    private <T> HttpMiddleware parseBody(Class<T> tClass) {
        return (req, resp, next) -> {
            InputStream is = (InputStream) req.body();
            try {
                var t = om.readValue(is, tClass);
                req.body(t);
                next.call();
            } catch (IOException e) {
                // TODO: handle error
            }
        };
    }

}
