package com.xyzwps.lib.express.middleware;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.lib.express.HttpStatus;
import lib.jshttp.mimedb.MimeDb;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class Static {

    private final String root;

    // TODO: 支持 prefix
    // TODO: cache

    public Static(String root) {
        this.root = Objects.requireNonNull(root);
    }

    public HttpMiddleware serve() {
        return (req, resp, next) -> {
            if (req.method() != HttpMethod.GET) {
                next.call();
                return;
            }

            var path = req.path();
            var lastPart = path.substring(path.lastIndexOf('/') + 1);
            var ext = lastPart.substring(lastPart.lastIndexOf('.') + 1);

            var $matchedMime = MimeDb.findFirstByExtension(ext);
            if ($matchedMime.isEmpty()) {
                next.call(); // no mime type matched
                return;
            }
            var mime = $matchedMime.get();

            var filePath = Path.of(root, req.path());
            if (!Files.exists(filePath)) {
                next.call();
                return;
            }

            try {
                var inputStream = Files.newInputStream(filePath);
                var buffer = new BufferedInputStream(inputStream);
                var allbytes = buffer.readAllBytes(); // TODO: 优化 getAllBBytes
                resp.status(HttpStatus.OK)
                        .header(HttpHeaders.CONTENT_TYPE, mime.essence())
                        .send(allbytes);
            } catch (Exception e) {
                // TODO: 处理错误
                next.call();
            }
        };
    }
}
