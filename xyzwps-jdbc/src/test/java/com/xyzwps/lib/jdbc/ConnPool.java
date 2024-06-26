package com.xyzwps.lib.jdbc;


import com.xyzwps.lib.jdbc.model.PlayableCharacter;
import com.xyzwps.lib.jdbc.model.PlayableCharacterDao;
import org.h2.jdbcx.JdbcConnectionPool;

import java.time.LocalDateTime;

import static com.xyzwps.lib.jdbc.model.Gender.*;
import static com.xyzwps.lib.jdbc.model.Region.*;
import static org.junit.jupiter.api.Assertions.*;

public final class ConnPool {

    public static final Database db = new Database(JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa"));

    static {
        db.tx(tx -> {
            try (var s = tx.createStatement()) {
                s.execute("""
                        CREATE TABLE users (
                            uid        bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            name       VARCHAR(255) NOT NULL,
                            region     VARCHAR(8) NOT NULL,
                            age        INT NOT NULL,
                            use_sword  BOOLEAN NOT NULL DEFAULT FALSE,
                            gender     enum('F', 'M'),
                            remark     varchar(20) DEFAULT NULL,
                            created_at TIMESTAMP NOT NULL
                        )""");
            }
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            var id1 = dao.insert(new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)));
            assertEquals(1, id1);
            var id2 = dao.insert(new PlayableCharacter(2, "Diona", MONDSTADT, 13, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)));
            assertEquals(2, id2);
            var id3 = dao.insert(new PlayableCharacter(3, "Eula", MONDSTADT, 22, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)));
            assertEquals(3, id3);
            var id4 = dao.insert(new PlayableCharacter(4, "Amber", MONDSTADT, 18, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)));
            assertEquals(4, id4);
            var id5 = dao.insert(new PlayableCharacter(5, "Navia", FONTAINE, 24, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)));
            assertEquals(5, id5);
            var id6 = dao.insert(new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hairs", LocalDateTime.of(2023, 10, 10, 12, 0, 0)));
            assertEquals(6, id6);
        });
    }
}
