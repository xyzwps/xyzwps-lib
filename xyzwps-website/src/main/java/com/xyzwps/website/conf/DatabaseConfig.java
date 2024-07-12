package com.xyzwps.website.conf;

import com.xyzwps.lib.jdbc.Database;
import com.zaxxer.hikari.HikariDataSource;
import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import io.avaje.inject.Primary;

@Factory
public class DatabaseConfig {

    @Bean
    @Primary
    public Database mainDb(Configurations conf) {
        return new Database(new HikariDataSource(conf.getHikariConfig("maindb")));
    }
}
