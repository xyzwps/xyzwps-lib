package com.xyzwps.lib.express;

public interface HttpResponse {

    HttpResponse status(int status);

    HttpResponse header(String name, String value);

    /**
     * TODO: 延迟发送
     * @param object
     */
    void send(String object);
}
