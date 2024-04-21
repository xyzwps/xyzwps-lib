package com.xyzwps.lib.express;

import static com.xyzwps.lib.express.HttpStatus.*;

public final class HttpException extends RuntimeException {

    public final Object payload;

    public final int status;

    public HttpException(String message, int status, Object payload) {
        super(message);
        this.payload = payload;
        this.status = status; // TODO: check valid status
    }

    public static HttpException badRequest(String message) {
        return new HttpException(message, BAD_REQUEST, null);
    }

    public static HttpException payloadTooLarge(String message, Object payload) {
        return new HttpException(message, BAD_REQUEST, payload);
    }
}
