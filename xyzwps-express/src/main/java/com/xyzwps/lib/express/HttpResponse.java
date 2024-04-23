package com.xyzwps.lib.express;

public interface HttpResponse {

    HttpResponse status(HttpStatus status);

    HttpResponse header(String name, String value);

    /**
     * TODO: 延迟发送
     */
    void send(byte[] bytes);
}
