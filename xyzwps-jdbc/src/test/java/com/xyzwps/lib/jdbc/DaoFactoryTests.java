package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.jdbc.model.PlayableCharacter;
import com.xyzwps.lib.jdbc.model.PlayableCharacterDao;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.xyzwps.lib.jdbc.model.Gender.*;
import static com.xyzwps.lib.jdbc.model.Region.*;
import static org.junit.jupiter.api.Assertions.*;

public class DaoFactoryTests {

    @Test
    void test() {
        ConnPool.db.tx(conn -> {
            var toBean = new ResultSetToBean();
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, toBean, conn);

            // region find all
            {
                var list = dao.findAll();
                assertIterableEquals(list, List.of(
                        new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                        new PlayableCharacter(2, "Diona", MONDSTADT, 13, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                        new PlayableCharacter(3, "Eula", MONDSTADT, 22, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                        new PlayableCharacter(4, "Amber", MONDSTADT, 18, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                        new PlayableCharacter(5, "Navia", FONTAINE, 24, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                        new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hairs", LocalDateTime.of(2023, 10, 10, 12, 0, 0))
                ));
            }
            // endregion

            // region find by id
            {
                var keqing = dao.findById(1);
                assertEquals(keqing, new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)));

                var unknown = dao.findById(1000000);
                assertNull(unknown);
            }
            // endregion

            // region find optional by id
            {
                var keqing = dao.findOptionalById(1);
                assertTrue(keqing.isPresent());
                assertEquals(keqing.get(), new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)));

                var unknown = dao.findOptionalById(1000000);
                assertFalse(unknown.isPresent());
            }
            // endregion

            // region count
            {
                var count = dao.count();
                assertEquals(count, 6);
            }
            // endregion

            // region count by region and gender
            {
                var count = dao.countByRegionAndGender("蒙德", F);
                assertEquals(count, 3);
            }
            // endregion

            // region update remark
            {
                assertEquals(1, dao.updateRemark(6, "Red hair"));
                assertEquals(dao.findById(6), new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hair", LocalDateTime.of(2023, 10, 10, 12, 0, 0)));

                assertEquals(1, dao.updateRemark(6, "Red hair"));
                assertEquals(dao.findById(6), new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hair", LocalDateTime.of(2023, 10, 10, 12, 0, 0)));

                dao.updateRemark(6, "Red hairs");
            }
            // endregion

        });
    }
}