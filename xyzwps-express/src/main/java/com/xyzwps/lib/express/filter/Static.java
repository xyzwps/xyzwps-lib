package com.xyzwps.lib.express.filter;

import com.xyzwps.lib.express.*;
import com.xyzwps.lib.http.MimeDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class Static {

    private static final Logger log = LoggerFactory.getLogger(Static.class);

    private final String rootDir;
    private final String pathPrefix;

    // TODO: cache

    public Static(String pathPrefix, String rootDir) {
        this.rootDir = Objects.requireNonNull(rootDir);

        if (pathPrefix == null || pathPrefix.isEmpty()) {
            this.pathPrefix = "/";
            return;
        }

        var prefixHPath = UrlPath.of(pathPrefix);
        if (prefixHPath.isPlain()) {
            this.pathPrefix = prefixHPath.toString() + '/';
        } else {
            throw new IllegalArgumentException("pathPrefix must be plain path");
        }
    }

    public Static(String rootDir) {
        this(null, rootDir);
    }

    public Filter serve() {
        return (req, resp, next) -> {
            if (req.method() != HttpMethod.GET) {
                next.next(req, resp);
                return;
            }

            var path = req.path();
            if (!path.startsWith(pathPrefix)) {
                next.next(req, resp);
                return;
            }

            var relativeFilePath = '/' + path.substring(pathPrefix.length());

            var lastPart = relativeFilePath.substring(relativeFilePath.lastIndexOf('/') + 1);
            var ext = lastPart.substring(lastPart.lastIndexOf('.') + 1);

            var $matchedMime = MimeDb.findFirstByExtension(ext);
            if ($matchedMime.isEmpty()) {
                next.next(req, resp); // no mime type matched
                return;
            }
            var mime = $matchedMime.get();

            var filePath = Path.of(rootDir, relativeFilePath);
            if (!Files.exists(filePath)) {
                next.next(req, resp);
                return;
            }

            try {
                var inputStream = Files.newInputStream(filePath);
                var buffer = new BufferedInputStream(inputStream);
                var allBytes = buffer.readAllBytes(); // TODO: 优化 getAllBBytes
                resp.ok();
                resp.headers().set(HttpHeaders.CONTENT_TYPE, mime.essence());
                resp.send(allBytes);
            } catch (Exception e) {
                log.error("Static filter error", e);
                resp.status(HttpStatus.INTERNAL_SERVER_ERROR);
                resp.headers().set(HttpHeaders.CONTENT_TYPE, "text/plain");
                resp.send("Internal Server Error".getBytes());
            }
        };
    }
}
