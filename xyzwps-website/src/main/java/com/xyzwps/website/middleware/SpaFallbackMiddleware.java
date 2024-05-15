package com.xyzwps.website.middleware;

import com.xyzwps.lib.express.HttpContext;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.website.modules.conf.Configurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class SpaFallbackMiddleware implements HttpMiddleware {

    private static final Logger log = LoggerFactory.getLogger(SpaFallbackMiddleware.class);

    private final Configurations conf;

    @Inject
    SpaFallbackMiddleware(Configurations conf) {
        this.conf = conf;
    }

    @Override
    public void call(HttpContext context) {
        var req = context.request();
        if (req.method() == HttpMethod.GET && !req.path().startsWith("/api")) {
            var bytes = getIndexDotHtml();
            if (bytes != null) {
                var resp = context.response();
                var headers = resp.headers();
                headers.set(HttpHeaders.CONTENT_TYPE, "text/html");
                context.response().send(bytes);
                return;
            }
        }

        context.next();
    }

    private byte[] getIndexDotHtml() {
        var path = Path.of(conf.getRouterStaticDirectory()).resolve("index.html");
        if (!Files.exists(path)) {
            log.warn("File {} does not exist.", path);
            return null;
        }

        try {
            return Files.readAllBytes(path);
        } catch (Exception e) {
            log.error("Read file {} error.", path, e);
            return null;
        }
    }
}
