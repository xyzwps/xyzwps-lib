package com.xyzwps.website.middleware;

import com.xyzwps.lib.express.middleware.BasicAuth;
import io.avaje.inject.Bean;
import io.avaje.inject.Factory;

@Factory
public class MiddlewareModule {

    @Bean
    public static BasicAuth provideBasicAuth() {
        return new BasicAuth((username, password) -> "scott".equals(username) && "tiger".equals(password));
    }
}
