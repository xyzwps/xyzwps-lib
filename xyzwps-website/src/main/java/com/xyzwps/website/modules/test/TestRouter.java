package com.xyzwps.website.modules.test;

import com.xyzwps.lib.express.filter.BasicAuth;
import com.xyzwps.lib.express.filter.Router;
import com.xyzwps.website.conf.Configurations;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class TestRouter extends Router.Nest {
    public TestRouter(Configurations conf, BasicAuth basicAuth, TestDao testDao) {
        this.get("count", new TestCountFilter(1) + new TestCountFilter(2) + new HelloWorldFilter());
    }
}
