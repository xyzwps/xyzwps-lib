package com.xyzwps.website.modules.conf;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import jakarta.inject.Singleton;


@Singleton
public class Configurations {

    private final Config config;

    public Configurations() {
        this.config = ConfigFactory.load("app");
    }

    public String getAppName() {
        return config.getString("app.name");
    }

    public String getRouterStaticDirectory() {
        return config.getString("app.router.static.dir");
    }

    public int getServerPort() {
        return config.getInt("app.server.port");
    }

    public HikariConfig getHikariConfig(String db) {
        var conf = new HikariConfig();
        conf.setJdbcUrl(config.getString("app." + db + ".url"));
        conf.setUsername(config.getString("app." + db + ".username"));
        conf.setPassword(config.getString("app." + db + ".password"));
        conf.setDriverClassName(config.getString("app." + db + ".driver"));
        return conf;
    }
}
