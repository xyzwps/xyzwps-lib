package com.xyzwps.lib.jdbc;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;


class DatabaseH2Tests {

    @Test
    void test() {
        var db = new Database(JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa"));
        init(db);
        new DaoFactoryTestCases(db).test();
    }

    void init(Database db) {
        var ctx = db.autoCommitTransactionContext();
        try (var s = ctx.createStatement()) {
            s.execute("""
                    CREATE TABLE playable_characters (
                        uid        bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        name       VARCHAR(255) NOT NULL,
                        region     VARCHAR(8) NOT NULL,
                        age        INT NOT NULL,
                        use_sword  BOOLEAN NOT NULL DEFAULT FALSE,
                        gender     enum('F', 'M'),
                        remark     varchar(20) DEFAULT NULL,
                        created_at TIMESTAMP NOT NULL
                    )""");
        } catch (SQLException e) {
            throw new DbException("Failed to init database", e);
        }
    }
}
