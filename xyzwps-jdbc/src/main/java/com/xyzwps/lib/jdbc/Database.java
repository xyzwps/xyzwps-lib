package com.xyzwps.lib.jdbc;

import javax.sql.DataSource;
import java.sql.*;

public class Database {

    private final DataSource ds;
    private final ResultSetToBean rs2b;

    public Database(DataSource ds) {
        this.ds = ds;
        this.rs2b = new ResultSetToBean();
    }

    public TransactionContext autoCommitTransactionContext() {
        try {
            var conn = ds.getConnection();
            conn.setAutoCommit(true);
            return TransactionContext.create(conn, rs2b);
        } catch (SQLException e) {
            throw new DbException("Failed to get connection", e);
        }
    }
}
