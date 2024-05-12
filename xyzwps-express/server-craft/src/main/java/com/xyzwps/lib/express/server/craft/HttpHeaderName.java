package com.xyzwps.lib.express.server.craft;

import com.xyzwps.lib.bedrock.Args;

public final class HttpHeaderName {
    public final String name;
    private final int hashCode;

    public HttpHeaderName(String name) {
        this.name = Args.notEmpty(name, "Invalid http header name");
        this.hashCode = name.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof HttpHeaderName httpHeaderName) {
            return this.name.equalsIgnoreCase(httpHeaderName.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
