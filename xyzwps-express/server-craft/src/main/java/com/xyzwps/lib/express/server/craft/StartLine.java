package com.xyzwps.lib.express.server.craft;

import com.xyzwps.lib.express.BadProtocolException;
import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpProtocol;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public record StartLine(HttpMethod method, String url, HttpProtocol protocol) {
    public StartLine {
        Objects.requireNonNull(method, "Invalid start line: no method");
        Objects.requireNonNull(url, "Invalid start line: no url");
        Objects.requireNonNull(protocol, "Invalid start line: no protocol");
    }

    public URI toURI() {
        return toURI(url);
    }

    private static URI toURI(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new BadProtocolException("Invalid url");
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", method, url, protocol);
    }
}