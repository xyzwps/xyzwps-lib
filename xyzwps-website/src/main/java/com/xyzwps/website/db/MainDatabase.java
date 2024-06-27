package com.xyzwps.website.db;

import com.xyzwps.lib.jdbc.Database;
import com.xyzwps.website.modules.conf.Configurations;
import com.zaxxer.hikari.HikariDataSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainDatabase extends Database {

    @Inject
    public MainDatabase(Configurations conf) {
        super(new HikariDataSource(conf.getHikariConfig("maindb")));
    }
}
