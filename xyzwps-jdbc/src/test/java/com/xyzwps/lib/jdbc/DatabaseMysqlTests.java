package com.xyzwps.lib.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;

class DatabaseMysqlTests {

    static MySQLContainer<?> mysqlContainer;

    @BeforeAll
    static void setUp() {
        mysqlContainer = new MySQLContainer<>("mysql:8.0.36");
        mysqlContainer.start();
    }

    @AfterAll
    static void tearDown() {
        mysqlContainer.stop();
    }

    @Test
    void test() {
        var conf = new HikariConfig();
        conf.setJdbcUrl(mysqlContainer.getJdbcUrl());
        conf.setUsername("test");
        conf.setPassword("test");
        conf.setDriverClassName("com.mysql.jdbc.Driver");

        var db = new Database(new HikariDataSource(conf));
        init(db);
        new DaoFactoryTestCases(db).test();
    }

    void init(Database db) {
        db.tx(tx -> {
            try (var s = tx.createStatement()) {
                s.execute("""
                        CREATE TABLE playable_characters (
                            uid        bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
                            name       VARCHAR(255) NOT NULL,
                            region     VARCHAR(8) NOT NULL,
                            age        INT NOT NULL,
                            use_sword  BOOLEAN NOT NULL DEFAULT FALSE,
                            gender     enum('F', 'M'),
                            remark     varchar(20) DEFAULT NULL,
                            created_at TIMESTAMP NOT NULL
                        )""");
            }
        });
    }
}
