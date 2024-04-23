package com.xyzwps.lib.express;

import com.xyzwps.lib.bedrock.Args;

import static com.xyzwps.lib.express.HttpStatus.*;

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
        return new HttpException(String.format(message, args), BAD_REQUEST, null);
    }

    public static HttpException payloadTooLarge(String message, Object payload) {
        return new HttpException(message, PAYLOAD_TOO_LARGE, payload);
    }
}
