package com.xyzwps.lib.jdbc;

import javax.sql.DataSource;

public final class Database {

    private final DataSource ds;

    public Database(DataSource ds) {
        this.ds = ds;
    }


}
