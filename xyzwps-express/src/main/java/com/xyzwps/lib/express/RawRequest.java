package com.xyzwps.lib.express;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.xyzwps.lib.dollar.Dollar.*;

public record RawRequest(StartLine startLine, List<HeaderLine> headerLines, InputStream in) {

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

    public record StartLine(HttpMethod method, String url, String protocol) {
        public StartLine {
            Objects.requireNonNull(method);
            Objects.requireNonNull(url);
            Objects.requireNonNull(protocol);
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", method, url, protocol);
        }
    }

    public HttpRequest<InputStream> toHttpRequest() {
        var headers = $(this.headerLines)
                .groupBy(HeaderLine::name)
                .mapValues((values, name) -> new HttpHeader(name, List.copyOf($.map(values, HeaderLine::value))))
                .toMap();
        return new SimpleHttpRequest<>(this.startLine.method, this.startLine.url, this.startLine.protocol, Map.copyOf(headers), this.in);
    }
}
