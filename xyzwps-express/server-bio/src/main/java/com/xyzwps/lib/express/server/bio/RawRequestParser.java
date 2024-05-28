package com.xyzwps.lib.express.server.bio;

import com.xyzwps.lib.dollar.Either;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.server.bio.util.CRLFLineCallbackReader;
import com.xyzwps.lib.express.server.commons.HeaderLineParser;
import com.xyzwps.lib.express.server.commons.SimpleHttpHeaders;
import com.xyzwps.lib.express.server.commons.StartLine;
import com.xyzwps.lib.express.server.commons.StartLineParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

class RawRequestParser {

    private final CRLFLineCallbackReader lineReader;

    RawRequestParser(InputStream in) {
        this.lineReader = new CRLFLineCallbackReader(in);
    }

    Either<String, HttpHeaders> headers() {
        var headers = new SimpleHttpHeaders();
        for (; ; ) {
            var text = lineReader.readLine(StandardCharsets.ISO_8859_1);
            if (text == null) {
                return ERR_INVALID_HEADER_LINE;
            }

            if (text.isEmpty()) {
                return Either.right(headers);
            }

            var either = HeaderLineParser.parse(text);
            if (either.isLeft()) {
                return Either.left(either.left());
            } else {
                var headerLine = either.right();
                headers.append(headerLine.name(), headerLine.value());
            }
        }
    }

    private static final Either<String, HttpHeaders> ERR_INVALID_HEADER_LINE = Either.left("Invalid header line");

    Either<String, StartLine> startLine() {
        return StartLineParser.parse(lineReader.readLine(StandardCharsets.ISO_8859_1));
    }

}
