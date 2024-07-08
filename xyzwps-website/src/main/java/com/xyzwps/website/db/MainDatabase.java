package com.xyzwps.website.db;

import com.xyzwps.lib.jdbc.Database;
import com.xyzwps.website.conf.Configurations;
import com.zaxxer.hikari.HikariDataSource;
import io.avaje.inject.Lazy;
import jakarta.inject.Singleton;

@Lazy
@Singleton
public class MainDatabase extends Database {

    public MainDatabase(Configurations conf) {
        super(new HikariDataSource(conf.getHikariConfig("maindb")));
    }
}
