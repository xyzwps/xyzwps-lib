package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.jdbc.model.PlayableCharacter;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.jdbc.model.Gender.*;
import static com.xyzwps.lib.jdbc.model.Region.*;

class ResultSetToBeanTests {

    @Test
    void test() {
        ConnPool.db.tx(tx -> {
            try(var stmt = tx.createStatement();
                var rs = stmt.executeQuery("SELECT * FROM users ORDER BY uid ASC")) {
                var toBean = new ResultSetToBean();
                var list = toBean.toList(rs, PlayableCharacter.class);
                assertIterableEquals(list, List.of(
                        new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                        new PlayableCharacter(2, "Diona", MONDSTADT, 13, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                        new PlayableCharacter(3, "Eula", MONDSTADT, 22, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                        new PlayableCharacter(4, "Amber", MONDSTADT, 18, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                        new PlayableCharacter(5, "Navia", FONTAINE, 24, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                        new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hairs", LocalDateTime.of(2023, 10, 10, 12, 0, 0))
                ));
            }
        });
    }

}
