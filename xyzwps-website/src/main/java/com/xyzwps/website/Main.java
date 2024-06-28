package com.xyzwps.website;

import io.avaje.inject.BeanScope;

public class Main {

    public static void main(String[] args) {
        BeanScope scope = BeanScope.builder().build();
        scope.get(HttpServerLayer.class).start();;
    }
}
