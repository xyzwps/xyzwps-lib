package com.xyzwps.website.middleware;

import com.xyzwps.lib.express.middleware.BasicAuth;
import com.xyzwps.lib.express.middleware.JsonParser;
import com.xyzwps.website.common.JSON;
import io.avaje.inject.Bean;
import io.avaje.inject.Factory;

@Factory
public class MiddlewareModule {

    @Bean
    public static BasicAuth provideBasicAuth() {
        return new BasicAuth((username, password) -> "scott".equals(username) && "tiger".equals(password));
    }

    @Bean
    public static JsonParser jsonParser() {
        return new JsonParser(JSON.JM);
    }
}
