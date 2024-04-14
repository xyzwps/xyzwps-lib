package com.xyzwps.lib.express;

public interface HttpResponse {

    HttpResponse status(int status);

    HttpResponse header(String name, String value);

    void send(String object);
}
