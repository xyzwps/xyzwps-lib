package com.xyzwps.lib.express.middleware;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.lib.express.middleware.router.HPath;
import lib.jshttp.mimedb.MimeDb;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class Static {

    private final String rootDir;
    private final String pathPrefix;

    // TODO: cache

    public Static(String pathPrefix, String rootDir) {
        this.rootDir = Objects.requireNonNull(rootDir);

        if (pathPrefix == null || pathPrefix.isEmpty()) {
            this.pathPrefix = "/";
            return;
        }

        var prefixHPath = HPath.from(pathPrefix);
        if (prefixHPath.isPlain()) {
            this.pathPrefix = prefixHPath.toString() + '/';
        } else {
            throw new IllegalArgumentException("pathPrefix must be plain path");
        }
    }

    public Static(String rootDir) {
        this(null, rootDir);
    }

    public HttpMiddleware serve() {
        return (ctx) -> {
            var req = ctx.request();
            var resp = ctx.response();

            if (req.method() != HttpMethod.GET) {
                ctx.next();
                return;
            }

            var path = req.path();
            if (!path.startsWith(pathPrefix)) {
                ctx.next();
                return;
            }

            var relativeFilePath = '/' + path.substring(pathPrefix.length());

            var lastPart = relativeFilePath.substring(relativeFilePath.lastIndexOf('/') + 1);
            var ext = lastPart.substring(lastPart.lastIndexOf('.') + 1);

            var $matchedMime = MimeDb.findFirstByExtension(ext);
            if ($matchedMime.isEmpty()) {
                ctx.next(); // no mime type matched
                return;
            }
            var mime = $matchedMime.get();

            var filePath = Path.of(rootDir, relativeFilePath);
            if (!Files.exists(filePath)) {
                ctx.next();
                return;
            }

            try {
                var inputStream = Files.newInputStream(filePath);
                var buffer = new BufferedInputStream(inputStream);
                var allbytes = buffer.readAllBytes(); // TODO: 优化 getAllBBytes
                resp.ok();
                resp.headers().set(HttpHeaders.CONTENT_TYPE, mime.essence());
                resp.send(allbytes);
            } catch (Exception e) {
                // TODO: 处理错误
                ctx.next();
            }
        };
    }
}
