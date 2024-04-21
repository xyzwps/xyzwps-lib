package com.xyzwps.lib.express;

public final class HttpStatus {

    public static final int BAD_REQUEST = 400;

    public static final int PAYLOAD_TOO_LARGE = 413;

    private HttpStatus() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
