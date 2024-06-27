package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.jdbc.model.PlayableCharacter;
import com.xyzwps.lib.jdbc.model.PlayableCharacterDao;

import java.time.LocalDateTime;
import java.util.List;

import static com.xyzwps.lib.jdbc.model.Gender.*;
import static com.xyzwps.lib.jdbc.model.Region.*;
import static org.junit.jupiter.api.Assertions.*;

record DaoFactoryTestCases(Database db) {

    void test() {
        insert();
        batchInsert();
        findAll();
        findById();
        findOptionalById();
        count();
        countByRegionAndGender();
        updateRemark();
    }

    void insert() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            var id1 = dao.insert(new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)));
            assertEquals(1, id1);
            var id2 = dao.insert(new PlayableCharacter(1, "Diona", MONDSTADT, 13, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)));
            assertEquals(2, id2);
            var id3 = dao.insert(new PlayableCharacter(1, "Eula", MONDSTADT, 22, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)));
            assertEquals(3, id3);
        });
    }

    void batchInsert() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            dao.batchInsert(List.of(
                    new PlayableCharacter(4, "Amber", MONDSTADT, 18, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(5, "Navia", FONTAINE, 24, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hairs", LocalDateTime.of(2023, 10, 10, 12, 0, 0))
            ));
        });
    }

    void findAll() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            var list = dao.findAll();
            assertIterableEquals(list, List.of(
                    new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(2, "Diona", MONDSTADT, 13, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(3, "Eula", MONDSTADT, 22, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(4, "Amber", MONDSTADT, 18, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(5, "Navia", FONTAINE, 24, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hairs", LocalDateTime.of(2023, 10, 10, 12, 0, 0))
            ));
        });
    }

    void findById() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);

            var keqing = dao.findById(1);
            assertEquals(keqing, new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)));

            var unknown = dao.findById(1000000);
            assertNull(unknown);
        });
    }

    void findOptionalById() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);

            var keqing = dao.findOptionalById(1);
            assertTrue(keqing.isPresent());
            assertEquals(keqing.get(), new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)));

            var unknown = dao.findOptionalById(1000000);
            assertFalse(unknown.isPresent());
        });
    }

    void count() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            var count = dao.count();
            assertEquals(count, 6);
        });
    }

    void countByRegionAndGender() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            var count = dao.countByRegionAndGender("蒙德", F);
            assertEquals(count, 3);
        });
    }

    void updateRemark() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);

            assertEquals(1, dao.updateRemark(6, "Red hair"));
            assertEquals(dao.findById(6), new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hair", LocalDateTime.of(2023, 10, 10, 12, 0, 0)));

            assertEquals(1, dao.updateRemark(6, "Red hair"));
            assertEquals(dao.findById(6), new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hair", LocalDateTime.of(2023, 10, 10, 12, 0, 0)));

            dao.updateRemark(6, "Red hairs");
        });
    }
}
