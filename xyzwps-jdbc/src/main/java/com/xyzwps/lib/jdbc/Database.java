package com.xyzwps.lib.jdbc;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class Database {

    private final DataSource ds;
    private final ResultSetToBean rs2b;

    public Database(DataSource ds) {
        this.ds = ds;
        this.rs2b = new ResultSetToBean();
    }

    public void tx(SqlConsumer<TX> handler) {
        try (var conn = ds.getConnection()) {
            var tx = new TX(conn, rs2b);
            try {
                conn.setAutoCommit(false);
                handler.accept(tx);
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
