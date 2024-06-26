package com.xyzwps.lib.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public final class Database {

    private final DataSource ds;

    public Database(DataSource ds) {
        this.ds = ds;
    }

    public void tx(SqlConsumer<Connection> handler) {
        try (var conn = ds.getConnection()) {
            try {
                conn.setAutoCommit(false);
                handler.accept(conn);
                conn.commit();
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException e2) {
                    throw new DbException("Rollback failed", e2);
                }
                throw new DbException("Transaction failed", e);
            }
        } catch (SQLException e) {
            throw new DbException("Failed to get connection", e);
        }
    }
}
