package com.xyzwps.lib.express;

import com.xyzwps.lib.bedrock.Args;

public final class HttpException extends RuntimeException {

    public final Object payload;

    public final HttpStatus status;

    public HttpException(String message, HttpStatus status, Object payload) {
        super(message);
        this.payload = payload;
        this.status = Args.notNull(status, "Http status cannot be null");
    }

    public HttpException(String message, HttpStatus status) {
        this(message, status, null);
    }


    public static HttpException badRequest(String message, Object... args) {
        return new HttpException(String.format(message, args), HttpStatus.BAD_REQUEST, null);
    }

    public static HttpException unauthorized(String message) {
        return new HttpException(message, HttpStatus.UNAUTHORIZED, null);
    }

    public static HttpException payloadTooLarge(String message, Object payload) {
        return new HttpException(message, HttpStatus.PAYLOAD_TOO_LARGE, payload);
    }
}
