package com.xyzwps.lib.express.server.bio;

import com.xyzwps.lib.dollar.Either;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpProtocol;
import com.xyzwps.lib.express.server.bio.util.CRLFLineCallbackReader;

import java.io.*;
import java.nio.charset.StandardCharsets;

class RawRequestParser {

    private final CRLFLineCallbackReader lineReader;

    RawRequestParser(InputStream in) {
        this.lineReader = new CRLFLineCallbackReader(in);
    }

    Either<String, HttpHeaders> headers() {
        var headers = new BioHttpHeaders();
        for (; ; ) {
            var text = lineReader.readLine(StandardCharsets.ISO_8859_1);
            if (text == null) {
                return ERR_INVALID_HEADER_LINE;
            }

            if (text.isEmpty()) {
                return Either.right(headers);
            }

            var segments = text.split(":", 2);
            if (segments.length != 2) {
                return ERR_INVALID_HEADER_LINE;
            }

            headers.append(segments[0], segments[1].trim());
        }
    }

    private static final Either<String, HttpHeaders> ERR_INVALID_HEADER_LINE = Either.left("Invalid header line");

    Either<String, StartLine> startLine() {
        var text = lineReader.readLine(StandardCharsets.ISO_8859_1);
        if (text == null) {
            return Either.left("Invalid start line");
        }

        var segments = text.split(" ", 3);
        if (segments.length != 3) {
            return Either.left("Invalid start line");
        }

        var $method = HttpMethod.from(segments[0]);
        if ($method.isLeft()) {
            return Either.left("Invalid start line: " + $method.left());
        }

        var method = $method.right();
        var path = segments[1];

        var $protocol = HttpProtocol.from(segments[2]);
        if ($protocol.isLeft()) {
            return Either.left("Invalid start line: " + $protocol.left());
        }

        try {
            return Either.right(new StartLine(method, path, $protocol.right()));
        } catch (Exception e) {
            return Either.left(e.getMessage());
        }
    }

}
