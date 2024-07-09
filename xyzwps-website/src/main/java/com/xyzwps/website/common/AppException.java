package com.xyzwps.website.common;

import com.xyzwps.lib.express.HttpStatus;

public class AppException extends RuntimeException {

    private final HttpStatus status;

    private AppException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus status() {
        return status;
    }

    public static AppException badRequest(String message) {
        return new AppException(HttpStatus.BAD_REQUEST, message);
    }

    public static AppException internalServerError(String message) {
        return new AppException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
