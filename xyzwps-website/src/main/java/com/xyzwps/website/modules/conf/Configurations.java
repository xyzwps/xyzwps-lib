package com.xyzwps.website.modules.conf;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Configurations {

    private final Config config;

    @Inject
    public Configurations() {
        this.config = ConfigFactory.load("app");
    }

    public String getRouterStaticDirectory() {
        return config.getString("app.router.static.dir");
    }

    public int getServerPort() {
        return config.getInt("app.server.port");
    }
}
