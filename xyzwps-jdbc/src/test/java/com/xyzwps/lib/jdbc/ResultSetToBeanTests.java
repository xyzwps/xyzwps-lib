package com.xyzwps.lib.jdbc;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResultSetToBeanTests {

    @Test
    void test() throws SQLException {
        var conn = ConnPool.getConnection();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery("SELECT * FROM users ORDER BY uid ASC");

        var toBean = new ResultSetToBean();
        var list = toBean.toList(rs, User.class);
        assertIterableEquals(list, List.of(
                new User(1, "Keqing", 17, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new User(2, "Diona", 13, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new User(3, "Eula", 22, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new User(4, "Amber", 18, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new User(5, "Navia", 24, LocalDateTime.of(2023, 10, 10, 12, 0, 0))
        ));
    }

    record User(@Column(name = "uid") long id, String name, int age, LocalDateTime createdAt) {
    }
}
