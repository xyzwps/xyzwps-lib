package com.xyzwps.website.db;

import com.xyzwps.lib.jdbc.Database;
import com.xyzwps.website.Configurations;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.inject.Singleton;

@Singleton
public class MainDatabase extends Database {

    public MainDatabase(Configurations conf) {
        super(new HikariDataSource(conf.getHikariConfig("maindb")));
    }
}
