package com.xyzwps.lib.express;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record RawRequest(StartLine startLine, List<HeaderLine> headerLines, RequestBodyPayload bodyPayload) {

    @Override
    public String toString() {
        var b = new ArrayList<String>();
        if (startLine != null) b.add(startLine.toString());
        if (headerLines != null) {
            for (var hl : headerLines) {
                b.add(hl.toString());
            }
        }
        return String.join("\n", b);
    }

    public static final class StartLine {
        public final HttpMethod method;
        public final String url;
        public final String protocol;

        public StartLine(HttpMethod method, String url, String protocol) {
            this.method = Objects.requireNonNull(method);
            this.url = Objects.requireNonNull(url);
            this.protocol = Objects.requireNonNull(protocol);
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", method, url, protocol);
        }
    }

    public static final class HeaderLine {
        public final String name;
        public final String value;

        public HeaderLine(String name, String value) {
            this.name = Objects.requireNonNull(name);
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public String toString() {
            return name + ": " + value;
        }
    }

    public record RequestBodyPayload(InputStream in) {
    }
}
