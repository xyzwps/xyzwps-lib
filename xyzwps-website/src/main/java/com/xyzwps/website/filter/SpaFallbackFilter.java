package com.xyzwps.website.filter;

import com.xyzwps.lib.express.*;
import com.xyzwps.lib.http.HttpMethod;
import com.xyzwps.website.conf.Configurations;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class SpaFallbackFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SpaFallbackFilter.class);

    private final Configurations conf;

    public SpaFallbackFilter(Configurations conf) {
        this.conf = conf;
    }

    @Override
    public void filter(HttpRequest req, HttpResponse resp, Next next) {
        if (req.method() == HttpMethod.GET && !req.path().startsWith("/api")) {
            var bytes = getIndexDotHtml();
            if (bytes != null) {
                var headers = resp.headers();
                headers.set(HttpHeaders.CONTENT_TYPE, "text/html");
                resp.send(bytes);
                return;
            }
        }

        next.next(req, resp);
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
