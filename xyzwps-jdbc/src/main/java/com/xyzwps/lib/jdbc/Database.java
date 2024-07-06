package com.xyzwps.lib.jdbc;

import javax.sql.DataSource;
import java.sql.SQLException;

public class Database {

    private final DataSource ds;
    private final ResultSetToBean rs2b;

    public Database(DataSource ds) {
        this.ds = ds;
        this.rs2b = new ResultSetToBean();
    }

    public void tx(SqlConsumer<TransactionContext> handler) {
        try (var conn = ds.getConnection()) {
            var tx = new TransactionContext(conn, rs2b);
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

    public <R> R tx(SqlFunction<TransactionContext, R> handler) {
        try (var conn = ds.getConnection()) {
            var tx = new TransactionContext(conn, rs2b);
            try {
                conn.setAutoCommit(false);
                var r = handler.apply(tx);
                conn.commit();
                return r;
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
