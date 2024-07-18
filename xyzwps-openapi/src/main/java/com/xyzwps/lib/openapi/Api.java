package com.xyzwps.lib.openapi;

public class Api {
    private final String method;
    private final String path;

    public Api(String method, String path) {
        this.method = method;
        this.path = path;
    }


    public void addRequestBody(Class<?> type) {
    }

    public void addHeaderParam(String name, Class<?> type) {
    }

    public void addPathParam(String name, Class<?> type) {
    }

    public void addSearchParam(String name, Class<?> type) {
    }
}
