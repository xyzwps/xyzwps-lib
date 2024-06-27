package com.xyzwps.website.middleware;

import com.xyzwps.lib.express.*;
import com.xyzwps.website.modules.conf.Configurations;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class SpaFallbackMiddleware implements HttpMiddleware {

    private static final Logger log = Logger.getLogger(LogRequestCostMiddleware.class);

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
            log.warnf("File %s does not exist.", path);
            return null;
        }

        try {
            return Files.readAllBytes(path);
        } catch (Exception e) {
            log.errorf(e, "Read file %s error.", path);
            return null;
        }
    }
}
