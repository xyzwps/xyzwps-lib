package com.xyzwps.website.middleware;

import com.xyzwps.lib.express.middleware.BasicAuth;
import dagger.Module;
import dagger.Provides;

@Module
public class MiddlewareModule {

    @Provides
    public static BasicAuth provideBasicAuth() {
        return new BasicAuth((username, password) -> "scott".equals(username) && "tiger".equals(password));
    }
}
