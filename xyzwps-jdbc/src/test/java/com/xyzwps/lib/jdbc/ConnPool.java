package com.xyzwps.lib.jdbc;


import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Connection;

public final class ConnPool {

    private static final DataSource ds = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa");

    static {
        try {
            var conn = ds.getConnection();
            conn.createStatement().execute("""
                    CREATE TABLE users (
                        uid        bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        name       VARCHAR(255) NOT NULL,
                        region     VARCHAR(8) NOT NULL,
                        age        INT NOT NULL,
                        use_sword  BOOLEAN NOT NULL DEFAULT FALSE,
                        gender     enum('F', 'M'),
                        remark     varchar(20) DEFAULT NULL,
                        created_at TIMESTAMP NOT NULL
                    )""");
            conn.createStatement().execute("""
                    INSERT INTO users (name, region, age, use_sword, gender, created_at) VALUES
                        ('Keqing', '璃月', 17, true,  'F', '2023-10-10 12:00:00'),
                        ('Diona',  '蒙德', 13, false, 'F', '2023-10-10 12:00:00'),
                        ('Eula',   '蒙德', 22, false, 'F', '2023-10-10 12:00:00'),
                        ('Amber',  '蒙德', 18, false, 'F', '2023-10-10 12:00:00'),
                        ('Navia',  '枫丹', 24, false, 'F', '2023-10-10 12:00:00')
                    """);

            conn.createStatement().execute("""
                    INSERT INTO users (name, region, age, use_sword, gender, remark, created_at) VALUES
                        ('Diluc', '蒙德', 27, false, 'M', 'Red hairs', '2023-10-10 12:00:00')
                    """);
            conn.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
