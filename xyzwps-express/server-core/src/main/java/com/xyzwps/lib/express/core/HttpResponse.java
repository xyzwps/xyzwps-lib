package com.xyzwps.lib.express.core;

/**
 * TODO: 做规定
 */
public interface HttpResponse {

    /**
     * return this
     */
    HttpResponse status(HttpStatus status);

    /**
     * TODO: 如何理解这个语义
     *
     * return this
     */
    HttpResponse header(String name, String value);

    void send(byte[] bytes);
}
