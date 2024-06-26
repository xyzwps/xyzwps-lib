package com.xyzwps.lib.jdbc;

import java.sql.SQLException;

public interface SqlConsumer<T> {

    void accept(T v) throws SQLException;
}
