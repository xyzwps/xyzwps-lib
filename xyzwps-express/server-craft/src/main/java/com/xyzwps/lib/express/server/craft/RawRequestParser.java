package com.xyzwps.lib.express.server.craft;

import com.xyzwps.lib.express.BadProtocolException;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.server.craft.util.CRLFLineCallbackReader;

import java.io.*;
import java.nio.charset.StandardCharsets;

class RawRequestParser {

    private final CRLFLineCallbackReader lineReader;

    RawRequestParser(InputStream in) {
        this.lineReader = new CRLFLineCallbackReader(in);
    }

    HttpHeaders headers() {
        var headers = new CraftHttpHeaders();
        for (; ; ) {
            var text = lineReader.readLine(StandardCharsets.ISO_8859_1);
            if (text == null) {
                throw new BadProtocolException("Invalid header line");
            }
            if (text.isEmpty()) {
                return headers;
            }

            var segments = text.split(":", 2);
            if (segments.length != 2) {
                throw new BadProtocolException("Invalid header line");
            }

            headers.append(segments[0], segments[1].trim());
        }
    }

    StartLine startLine() {
        var text = lineReader.readLine(StandardCharsets.ISO_8859_1);
        if (text == null) {
            throw new BadProtocolException("Invalid start line");
        }

        var segments = text.split(" ", 3);
        if (segments.length != 3) {
            throw new BadProtocolException("Invalid start line");
        }

        try {
            var method = HttpMethod.from(segments[0]);
            var path = segments[1];
            var protocol = segments[2];
            return new StartLine(method, path, protocol);
        } catch (Exception e) {
            throw new BadProtocolException("Invalid start line");
        }
    }

}
