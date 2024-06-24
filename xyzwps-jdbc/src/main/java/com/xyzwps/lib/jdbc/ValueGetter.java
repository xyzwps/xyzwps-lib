package com.xyzwps.lib.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A functional interface for getting values from a {@link ResultSet}.
 */
public interface ValueGetter {
    /**
     * Get a value from a {@link ResultSet}.
     *
     * @param rs     the {@link ResultSet} to get the value from
     * @param column the name of the column to get the value from
     * @return the value from the {@link ResultSet}
     * @throws SQLException if a database access error occurs
     */
    Object get(ResultSet rs, String column) throws SQLException;

    /**
     * A getter that does nothing.
     */
    enum None implements ValueGetter {
        ;

        @Override
        public Object get(ResultSet rs, String column) {
            throw new RuntimeException("This getter is not supposed to be called.");
        }
    }
}