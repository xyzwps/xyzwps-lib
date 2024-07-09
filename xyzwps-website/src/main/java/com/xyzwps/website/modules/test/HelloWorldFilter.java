package com.xyzwps.website.modules.test;

import com.xyzwps.lib.express.Filter;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;

import java.util.Map;

public class HelloWorldFilter implements Filter {
    @Override
    public void filter(HttpRequest request, HttpResponse response, Next next) {
        response.sendJson(Map.of("Hello", "World"));
    }
}
