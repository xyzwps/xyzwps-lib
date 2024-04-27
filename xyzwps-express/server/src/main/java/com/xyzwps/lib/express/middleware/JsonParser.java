package com.xyzwps.lib.express.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpMiddleware;
import lib.jsdom.mimetype.MimeType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class JsonParser {

    private final ObjectMapper om;

    public JsonParser(ObjectMapper om) {
        this.om = Args.notNull(om, "ObjectMapper cannot be null");
    }

    public <T> HttpMiddleware json(Class<T> tClass) {
        return (req, resp, next) -> {
            var contentType = req.contentType();

            if (contentType == null) {
                next.call();
                return;
            }

            if (!contentType.isApplicationJson()) {
                next.call();
                return;
            }

            if (!(req.body() instanceof InputStream)) {
                next.call();
                return;
            }

            parseBody(tClass, contentType).call(req, resp, next);
        };
    }

    private <T> HttpMiddleware parseBody(Class<T> tClass, MimeType type) {
        return (req, resp, next) -> {
            InputStream is = (InputStream) req.body();
            try {
                var charset = type.parameters.get("charset").map(Charset::forName).orElse(StandardCharsets.UTF_8);
                var reader = new InputStreamReader(is, charset);
                var t = om.readValue(reader, tClass);
                req.body(t);
                next.call();
            } catch (IOException e) {
                // TODO: handle error
            }
        };
    }

}
