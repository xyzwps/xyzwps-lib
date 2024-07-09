package com.xyzwps.lib.express;

public interface Handler {

    void handle(HttpRequest request, HttpResponse response);

    default Filter toFilter() {
        return new HandlerFilter(this);
    }
}
