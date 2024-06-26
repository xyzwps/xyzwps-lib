package com.xyzwps.lib.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ColumnPropertyMapper<P> {

    P fromColumn(ResultSet rs, String column) throws SQLException;

    void fromProperty(PreparedStatement ps, int index, P property) throws SQLException;

    enum None implements ColumnPropertyMapper<Object> {
        ;

        @Override
        public Object fromColumn(ResultSet rs, String column) {
            throw new RuntimeException("This mapper is not supposed to be called.");
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, Object property) {
            throw new RuntimeException("This mapper is not supposed to be called.");
        }
    }
}
