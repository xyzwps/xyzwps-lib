package com.xyzwps.lib.express.middleware;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.Filter;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.json.JsonException;
import com.xyzwps.lib.json.JsonMapper;
import lib.jsdom.mimetype.MimeType;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class JsonParser {

    private final JsonMapper jm;

    public JsonParser(JsonMapper jm) {
        this.jm = Args.notNull(jm, "JsonMapper cannot be null");
    }

    public <T> Filter json(Class<T> tClass) {
        return (req, resp, next) -> {
            var contentType = req.contentType();

            if (contentType == null) {
                next.next(req, resp);
                return;
            }

            if (!contentType.isApplicationJson()) {
                next.next(req, resp);
                return;
            }

            if (!(req.body() instanceof InputStream)) {
                next.next(req, resp);
                return;
            }

            parseBody(tClass, contentType).filter(req, resp, next);
        };
    }

    private <T> Filter parseBody(Class<T> tClass, MimeType type) {
        return (req, resp, next) -> {
            var is = (InputStream) req.body();
            try {
                var charset = type.parameters.get("charset").map(Charset::forName).orElse(StandardCharsets.UTF_8);
                var reader = new InputStreamReader(is, charset);
                var t = jm.parse(reader, tClass);
                req.body(t);
                next.next(req, resp);
            } catch (JsonException e) {
                // TODO: handle error
            }
        };
    }

}
