package com.xyzwps.lib.express;

public interface Handler {

    void handle(HttpRequest request, HttpResponse response);

    default Filter toFilter() {
        return (request, response, next) -> handle(request, response);
    }
}
