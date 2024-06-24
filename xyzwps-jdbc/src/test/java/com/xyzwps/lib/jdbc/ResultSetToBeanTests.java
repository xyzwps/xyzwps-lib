package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.jdbc.model.PlayableCharacter;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.jdbc.model.Gender.*;

class ResultSetToBeanTests {

    @Test
    void test() throws SQLException {
        var conn = ConnPool.getConnection();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery("SELECT * FROM users ORDER BY uid ASC");

        var toBean = new ResultSetToBean();
        var list = toBean.toList(rs, PlayableCharacter.class);
        assertIterableEquals(list, List.of(
                new PlayableCharacter(1, "Keqing", 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new PlayableCharacter(2, "Diona", 13, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new PlayableCharacter(3, "Eula", 22, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new PlayableCharacter(4, "Amber", 18, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new PlayableCharacter(5, "Navia", 24, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new PlayableCharacter(6, "Diluc", 27, false, M, "Red hairs", LocalDateTime.of(2023, 10, 10, 12, 0, 0))
        ));
    }


}
