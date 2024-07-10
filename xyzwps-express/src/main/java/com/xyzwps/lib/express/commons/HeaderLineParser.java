package com.xyzwps.lib.express.commons;

import com.xyzwps.lib.dollar.Either;

public final class HeaderLineParser {

    private static final Either<String, HeaderLine> ERR_INVALID_HEADER_LINE = Either.left("Invalid header line");

    public static Either<String, HeaderLine> parse(String text) {
        if (text == null || text.isEmpty()) {
            return ERR_INVALID_HEADER_LINE;
        }

        var segments = text.split(":", 2);
        if (segments.length != 2) {
            return ERR_INVALID_HEADER_LINE;
        }

        return Either.right(new HeaderLine(segments[0], segments[1].trim()));
    }
}
