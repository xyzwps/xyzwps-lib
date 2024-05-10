package com.xyzwps.lib.express.server.craft;

import com.xyzwps.lib.express.BadProtocolException;
import com.xyzwps.lib.express.HttpMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public record StartLine(HttpMethod method, String url, String protocol) {
    public StartLine {
        Objects.requireNonNull(method);
        Objects.requireNonNull(url);
        Objects.requireNonNull(protocol);
        // TODO: 检查 开始行
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