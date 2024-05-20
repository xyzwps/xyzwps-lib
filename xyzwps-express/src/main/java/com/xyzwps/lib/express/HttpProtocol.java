package com.xyzwps.lib.express;

import com.xyzwps.lib.dollar.Either;

public enum HttpProtocol {
    HTTP_1_1("HTTP/1.1");

    HttpProtocol(String value) {
        this.value = value;
    }

    public final String value;

    public static Either<String, HttpProtocol> from(String str) {
        if (str == null || str.isBlank()) {
            return ERR_EMPTY;
        }

        if ("HTTP/1.1".equalsIgnoreCase(str)) {
            return E_HTTP_1_1;
        }

        return Either.left("Invalid http protocol: " + str);
    }

    private static final Either<String, HttpProtocol> ERR_EMPTY = Either.left("Invalid http protocol: empty");
    private static final Either<String, HttpProtocol> E_HTTP_1_1 = Either.right(HTTP_1_1);

}
