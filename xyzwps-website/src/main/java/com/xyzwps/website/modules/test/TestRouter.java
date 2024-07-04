package com.xyzwps.website.modules.test;

import com.xyzwps.lib.express.filter.Router;
import jakarta.inject.Singleton;

@Singleton
public class TestRouter extends Router.Nest {

    public TestRouter() {
        this.get("count", new TestCountFilter(2)
                        .andThen(new TestCountFilter(3))
                        .andThen((req, resp, next) -> {
                            resp.ok();
                            resp.send("Hello, World!".getBytes());
                        }));
    }

}
