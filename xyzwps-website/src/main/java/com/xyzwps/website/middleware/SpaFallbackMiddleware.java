package com.xyzwps.website.middleware;

import com.xyzwps.lib.express.HttpContext;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.website.modules.conf.Configurations;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class SpaFallbackMiddleware implements HttpMiddleware {

    private final Configurations conf;

    @Inject
    SpaFallbackMiddleware(Configurations conf) {
        this.conf = conf;
    }

    @Override
    public void call(HttpContext context) {
        var req = context.request();
        if (req.method() == HttpMethod.GET && !req.path().startsWith("/api")) {
            var path = Path.of(conf.getRouterStaticDirectory()).resolve("index.html");
            if (Files.exists(path)) {
                try {
                    var bytes = Files.readAllBytes(path);
                    var resp = context.response();
                    var headers = resp.headers();
                    headers.set(HttpHeaders.CONTENT_TYPE, "text/html");
                    context.response().send(bytes);
                } catch (IOException e) {
                    throw new UncheckedIOException(e); // TODO: 处理错误
                }
            } else {
                context.next();
            }
        } else {
            context.next();
        }
    }

}
