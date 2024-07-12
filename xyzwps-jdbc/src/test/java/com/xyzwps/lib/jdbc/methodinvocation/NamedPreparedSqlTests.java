package com.xyzwps.lib.jdbc.methodinvocation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NamedPreparedSqlTests {

    @Test
    void duplicatedNames() {
        var sql = "SELECT * FROM table WHERE id = :value and name = :value";
        var result = NamedPreparedSql.create(sql);
        assertEquals("SELECT * FROM table WHERE id = ? and name = ?", result.sql());
        assertIterableEquals(List.of("value", "value"), result.names());
    }

    @Test
    void differentNames() {
        var sql = "SELECT * FROM table WHERE id = :id and name = :u.name";
        var result = NamedPreparedSql.create(sql);
        assertEquals("SELECT * FROM table WHERE id = ? and name = ?", result.sql());
        assertIterableEquals(List.of("id", "u.name"), result.names());
    }

    @Test
    void justPlaceholders() {
        var sql = "SELECT * FROM table WHERE id = ? and name = ?";
        var result = NamedPreparedSql.create(sql);
        assertEquals("SELECT * FROM table WHERE id = ? and name = ?", result.sql());
        assertTrue(result.names().isEmpty());
    }
}
