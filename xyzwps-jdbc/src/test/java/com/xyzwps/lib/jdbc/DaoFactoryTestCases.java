package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.jdbc.model.PlayableCharacter;
import com.xyzwps.lib.jdbc.model.PlayableCharacterDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import static com.xyzwps.lib.jdbc.model.Gender.*;
import static com.xyzwps.lib.jdbc.model.Region.*;
import static org.junit.jupiter.api.Assertions.*;

record DaoFactoryTestCases(Database db) {

    void test() {
        insert();
        findByName();
        batchInsert();
        findAll();
        getByRegionInOrderByUid();
        getByRegions();
        findOrderByAge();
        findByRegionOrderByAgeDesc();
        findById();
        findOptionalById();
        count();
        countByRegionAndGender();
        countByRegion();
        updateRemark();
        updateSetRemarkByUid();
        deleteByUid();
    }

    void findByName() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            assertIterableEquals(dao.findByName("Keqing"),
                    List.of(new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0))));
            assertTrue(dao.findByName("Nahida").isEmpty());
        });
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
        Consumer<List<PlayableCharacter>> assertAll = (list) -> assertIterableEquals(list, List.of(
                new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new PlayableCharacter(2, "Diona", MONDSTADT, 13, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new PlayableCharacter(3, "Eula", MONDSTADT, 22, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new PlayableCharacter(4, "Amber", MONDSTADT, 18, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new PlayableCharacter(5, "Navia", FONTAINE, 24, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hairs", LocalDateTime.of(2023, 10, 10, 12, 0, 0))
        ));

        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            assertAll.accept(dao.findAll());
            assertAll.accept(dao.find());
            assertAll.accept(dao.get());
            assertAll.accept(dao.getAll());
        });
    }

    void getByRegionInOrderByUid() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            assertIterableEquals(dao.getByRegionInOrderByUid(List.of("枫丹", "璃月")), List.of(
                    new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(5, "Navia", FONTAINE, 24, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0))
            ));
        });
    }

    void getByRegions() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            assertIterableEquals(dao.getByRegions(List.of("枫丹", "璃月")), List.of(
                    new PlayableCharacter(5, "Navia", FONTAINE, 24, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0))
            ));
        });
    }

    void findOrderByAge() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            assertIterableEquals(dao.findOrderByAge(), List.of(
                    new PlayableCharacter(2, "Diona", MONDSTADT, 13, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(1, "Keqing", LIYUE, 17, true, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(4, "Amber", MONDSTADT, 18, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(3, "Eula", MONDSTADT, 22, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(5, "Navia", FONTAINE, 24, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hairs", LocalDateTime.of(2023, 10, 10, 12, 0, 0))
            ));
        });
    }

    void findByRegionOrderByAgeDesc() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            assertIterableEquals(dao.findByRegionOrderByAgeDesc("蒙德"), List.of(
                    new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hairs", LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(3, "Eula", MONDSTADT, 22, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(4, "Amber", MONDSTADT, 18, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0)),
                    new PlayableCharacter(2, "Diona", MONDSTADT, 13, false, F, null, LocalDateTime.of(2023, 10, 10, 12, 0, 0))
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
            assertEquals(6, dao.count());
            assertEquals(6, dao.countAll());
        });
    }

    void countByRegionAndGender() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            var count = dao.countByRegionAndGender("蒙德", F);
            assertEquals(count, 3);
        });
    }

    void countByRegion() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            assertEquals(4, dao.countByRegion("蒙德"));
            assertEquals(1, dao.countByRegion("璃月"));
            assertEquals(1, dao.countByRegion("枫丹"));
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

    void updateSetRemarkByUid() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);

            dao.updateSetRemarkByUid("Red hair", 6);
            assertEquals(dao.findById(6), new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hair", LocalDateTime.of(2023, 10, 10, 12, 0, 0)));

            dao.updateSetRemarkByUid("Red hair", 6);
            assertEquals(dao.findById(6), new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hair", LocalDateTime.of(2023, 10, 10, 12, 0, 0)));

            dao.updateSetRemarkByUid("Red hairs", 6);
        });
    }

    void deleteByUid() {
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            assertEquals(dao.findById(6), new PlayableCharacter(6, "Diluc", MONDSTADT, 27, false, M, "Red hairs", LocalDateTime.of(2023, 10, 10, 12, 0, 0)));
        });
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            dao.deleteByUid(6);
            assertNull(dao.findById(6));
        });
        db.tx(tx -> {
            var dao = DaoFactory.createDao(PlayableCharacterDao.class, tx);
            assertNull(dao.findById(6));
        });
    }
}
