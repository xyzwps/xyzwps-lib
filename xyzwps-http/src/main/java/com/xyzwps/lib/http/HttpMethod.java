package com.xyzwps.lib.http;

import com.xyzwps.lib.dollar.Either;

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

    public static Either<String, HttpMethod> from(String str) {
        if (str == null || str.isBlank()) {
            return ERR_EMPTY;
        }

        try {
            return Either.right(HttpMethod.valueOf(str.toUpperCase()));
        } catch (Exception e) {
            return Either.left("Invalid http method: " + str);
        }
    }

    private static final Either<String, HttpMethod> ERR_EMPTY = Either.left("Invalid http method: empty");
}
