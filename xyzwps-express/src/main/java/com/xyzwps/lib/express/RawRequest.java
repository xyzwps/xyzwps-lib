package com.xyzwps.lib.express;


import com.xyzwps.lib.express.common.ContentLengthInputStream;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

record RawRequest(StartLine startLine, List<HeaderLine> headerLines, InputStream in) {

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

    public HttpRequest toHttpRequest() {
        var headers = new HttpHeaders();
        for (var header : headerLines) {
            headers.set(header.name(), header.value());
        }

        var uri = toURI(this.startLine.url);



        return new HttpRequest(this.startLine.method, uri, this.startLine.protocol,
                headers,
                new ContentLengthInputStream(this.in, BUFFER_LEN, headers.contentLength())
        );
    }

    private static final int BUFFER_LEN = 2048;

    private static URI toURI(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException("XXX"); // TODO: 真抛出了会怎样？
        }
    }
}
