package com.xyzwps.lib.express.core;

import java.util.Objects;

public enum HttpMethod {
    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    CONNECT,
    OPTIONS,
    TRACE,
    PATCH;

    public static HttpMethod from(String str) {
        return HttpMethod.valueOf(Objects.requireNonNull(str).toUpperCase());
    }
}
