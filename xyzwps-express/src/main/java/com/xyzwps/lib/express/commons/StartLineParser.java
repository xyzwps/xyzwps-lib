package com.xyzwps.lib.express.commons;

import com.xyzwps.lib.dollar.Either;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpProtocol;

public final class StartLineParser {

    public static Either<String, StartLine> parse(String text) {
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
