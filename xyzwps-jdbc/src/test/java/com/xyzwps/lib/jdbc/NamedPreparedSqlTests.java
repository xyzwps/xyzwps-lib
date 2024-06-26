package com.xyzwps.lib.jdbc;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NamedPreparedSqlTests {

    @Test
    void test() {
        // region test 1
        {
            var sql = "SELECT * FROM table WHERE id = :id and name = :u.name";
            var result = NamedPreparedSql.create(sql);
            assertEquals("SELECT * FROM table WHERE id = ? and name = ?", result.sql());
            assertIterableEquals(List.of("id", "u.name"), result.names());
        }
        // endregion

        // region test 2
        {
            var sql = "SELECT * FROM table WHERE id = :value and name = :value";
            var result = NamedPreparedSql.create(sql);
            assertEquals("SELECT * FROM table WHERE id = ? and name = ?", result.sql());
            assertIterableEquals(List.of("value", "value"), result.names());
        }
        // endregion

        // region test 3
        {
            var sql = "SELECT * FROM table WHERE id = ? and name = ?";
            var result = NamedPreparedSql.create(sql);
            assertEquals("SELECT * FROM table WHERE id = ? and name = ?", result.sql());
            assertTrue(result.names().isEmpty());
        }
        // endregion
    }
}
