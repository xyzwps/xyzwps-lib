package com.xyzwps.website.filter;

import com.xyzwps.lib.express.filter.BasicAuth;
import com.xyzwps.lib.express.filter.JsonParser;
import com.xyzwps.website.common.JSON;
import io.avaje.inject.Bean;
import io.avaje.inject.Factory;

@Factory
public class FilterModule {

    @Bean
    public static BasicAuth provideBasicAuth() {
        return new BasicAuth((username, password) -> "scott".equals(username) && "tiger".equals(password));
    }

    @Bean
    public static JsonParser jsonParser() {
        return new JsonParser(JSON.JM);
    }
}
