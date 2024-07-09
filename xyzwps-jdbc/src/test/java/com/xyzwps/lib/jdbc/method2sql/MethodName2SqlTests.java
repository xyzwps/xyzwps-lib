package com.xyzwps.lib.jdbc.method2sql;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MethodName2SqlTests {


    @Nested
    class SetPartCases {
        @Test
        void updateSetStatus() {
            var sql = MethodName2Sql.getSql("updateSetStatus", "user");
            assertEquals("UPDATE user SET status = ?", sql.sql());
            assertEquals(SqlType.UPDATE, sql.sqlType());
        }

        @Test
        void updateSetStatusAndCreatedAt() {
            var sql = MethodName2Sql.getSql("updateSetStatusAndCreatedAt", "user");
            assertEquals("UPDATE user SET status = ?, created_at = ?", sql.sql());
            assertEquals(SqlType.UPDATE, sql.sqlType());
        }

        @Test
        void updateSet() {
            assertThrows(Exception.class, () -> MethodName2Sql.getSql("updateSet", "user"));
        }
    }

    @Nested
    class LimitPartCases {

        @Test
        void findLimit() {
            var sql = MethodName2Sql.getSql("findLimit", "user");
            assertEquals("SELECT * FROM user LIMIT ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class OrderByCases {

        @Test
        void findOrderById() {
            var sql = MethodName2Sql.getSql("findOrderById", "user");
            assertEquals("SELECT * FROM user ORDER BY id ASC", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findOrderByIdDesc() {
            var sql = MethodName2Sql.getSql("findOrderByIdDesc", "user");
            assertEquals("SELECT * FROM user ORDER BY id DESC", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findOrderByIdAscAgeDesc() {
            var sql = MethodName2Sql.getSql("findOrderByIdAscAgeDesc", "user");
            assertEquals("SELECT * FROM user ORDER BY id ASC, age DESC", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findOrderByIdAscAge() {
            var sql = MethodName2Sql.getSql("findOrderByIdAscAge", "user");
            assertEquals("SELECT * FROM user ORDER BY id ASC, age ASC", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class IsNotNullCases {

        @Test
        void findByStarIsNotNull() {
            var sql = MethodName2Sql.getSql("findByStarIsNotNull", "user");
            assertEquals("SELECT * FROM user WHERE star IS NOT NULL", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findByStarNotNull() {
            var sql = MethodName2Sql.getSql("findByStarNotNull", "user");
            assertEquals("SELECT * FROM user WHERE star IS NOT NULL", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findByStarIsNotNullAndAgeNotNull() {
            var sql = MethodName2Sql.getSql("findByStarIsNotNullAndAgeNotNull", "user");
            assertEquals("SELECT * FROM user WHERE star IS NOT NULL AND age IS NOT NULL", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findByStarIsNotNullOrAgeNotNullAndGenderNotNull() {
            var sql = MethodName2Sql.getSql("findByStarIsNotNullOrAgeNotNullAndGenderNotNull", "user");
            assertEquals("SELECT * FROM user WHERE star IS NOT NULL OR age IS NOT NULL AND gender IS NOT NULL", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class IsNullCases {
        @Test
        void findByStarIsNull() {
            var sql = MethodName2Sql.getSql("findByStarIsNull", "user");
            assertEquals("SELECT * FROM user WHERE star IS NULL", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findByStarNull() {
            var sql = MethodName2Sql.getSql("findByStarNull", "user");
            assertEquals("SELECT * FROM user WHERE star IS NULL", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findByStarIsNullAndAgeNull() {
            var sql = MethodName2Sql.getSql("findByStarIsNullAndAgeNull", "user");
            assertEquals("SELECT * FROM user WHERE star IS NULL AND age IS NULL", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findByStarIsNullOrAgeNullAndGenderNull() {
            var sql = MethodName2Sql.getSql("findByStarIsNullOrAgeNullAndGenderNull", "user");
            assertEquals("SELECT * FROM user WHERE star IS NULL OR age IS NULL AND gender IS NULL", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class NotBetweenAndCases {

        @Test
        void findByAgeNotBetweenAnd() {
            var sql = MethodName2Sql.getSql("findByAgeNotBetweenAnd", "user");
            assertEquals("SELECT * FROM user WHERE age NOT BETWEEN ? AND ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findByAgeNotBetweenAndAndGender() {
            var sql = MethodName2Sql.getSql("findByAgeNotBetweenAndAndGender", "user");
            assertEquals("SELECT * FROM user WHERE age NOT BETWEEN ? AND ? AND gender = ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class BetweenCases {
        @Test
        void findByAgeBetweenAnd() {
            var sql = MethodName2Sql.getSql("findByAgeBetweenAnd", "user");
            assertEquals("SELECT * FROM user WHERE age BETWEEN ? AND ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findByAgeBetweenAndAndGender() {
            var sql = MethodName2Sql.getSql("findByAgeBetweenAndAndGender", "user");
            assertEquals("SELECT * FROM user WHERE age BETWEEN ? AND ? AND gender = ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class NotInCases {

        @Test
        void findByAgeNotIn() {
            var sql = MethodName2Sql.getSql("findByAgeNotIn", "user");
            assertEquals("SELECT * FROM user WHERE age NOT IN (?)", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class InCases {

        @Test
        void findByAgeIn() {
            var sql = MethodName2Sql.getSql("findByAgeIn", "user");
            assertEquals("SELECT * FROM user WHERE age IN (?)", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class NotLikeCases {
        @Test
        void findByDisplayNameNotLike() {
            var sql = MethodName2Sql.getSql("findByDisplayNameNotLike", "user");
            assertEquals("SELECT * FROM user WHERE display_name NOT LIKE ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class LikeCases {
        @Test
        void findByDisplayNameLike() {
            var sql = MethodName2Sql.getSql("findByDisplayNameLike", "user");
            assertEquals("SELECT * FROM user WHERE display_name LIKE ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class LeCases {
        @Test
        void findByAgeLe() {
            var sql = MethodName2Sql.getSql("findByAgeLe", "user");
            assertEquals("SELECT * FROM user WHERE age <= ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class LtCases {
        @Test
        void findByAgeLt() {
            var sql = MethodName2Sql.getSql("findByAgeLt", "user");
            assertEquals("SELECT * FROM user WHERE age < ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class GeCases {
        @Test
        void findByAgeGe() {
            var sql = MethodName2Sql.getSql("findByAgeGe", "user");
            assertEquals("SELECT * FROM user WHERE age >= ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class GtCases {
        @Test
        void findByAgeGt() {
            var sql = MethodName2Sql.getSql("findByAgeGt", "user");
            assertEquals("SELECT * FROM user WHERE age > ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class NeCases {
        @Test
        void findByAgeNe() {
            var sql = MethodName2Sql.getSql("findByAgeNe", "user");
            assertEquals("SELECT * FROM user WHERE age != ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class EqCases {
        @Test
        void findByAgeEq() {
            var sql = MethodName2Sql.getSql("findByAgeEq", "user");
            assertEquals("SELECT * FROM user WHERE age = ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findByAge() {
            var sql = MethodName2Sql.getSql("findByAgeEq", "user");
            assertEquals("SELECT * FROM user WHERE age = ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }


    @Nested
    class AndCases {
        @Test
        void findByAgeAndGender() {
            var sql = MethodName2Sql.getSql("findByAgeAndGender", "user");
            assertEquals("SELECT * FROM user WHERE age = ? AND gender = ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class OrCases {
        @Test
        void findByAgeOrGender() {
            var sql = MethodName2Sql.getSql("findByAgeOrGender", "user");
            assertEquals("SELECT * FROM user WHERE age = ? OR gender = ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }

    @Nested
    class DeleteCases {

        @Test
        void deleteByAge() {
            var sql = MethodName2Sql.getSql("deleteByAge", "user");
            assertEquals("DELETE FROM user WHERE age = ?", sql.sql());
            assertEquals(SqlType.DELETE, sql.sqlType());
        }

        @Test
        void deleteByAgeAndName() {
            var sql = MethodName2Sql.getSql("deleteByAgeAndName", "user");
            assertEquals("DELETE FROM user WHERE age = ? AND name = ?", sql.sql());
            assertEquals(SqlType.DELETE, sql.sqlType());
        }

        @Test
        void delete() {
            var sql = MethodName2Sql.getSql("delete", "user");
            assertEquals("DELETE FROM user", sql.sql());
            assertEquals(SqlType.DELETE, sql.sqlType());
        }
    }

    @Nested
    class UpdateCases {

        @Test
        void updateSetAge() {
            var sql = MethodName2Sql.getSql("updateSetAge", "user");
            assertEquals("UPDATE user SET age = ?", sql.sql());
            assertEquals(SqlType.UPDATE, sql.sqlType());
        }

        @Test
        void updateSetAgeAndName() {
            var sql = MethodName2Sql.getSql("updateSetAgeAndName", "user");
            assertEquals("UPDATE user SET age = ?, name = ?", sql.sql());
            assertEquals(SqlType.UPDATE, sql.sqlType());
        }

        @Test
        void updateSetAgeAndNameWhereId() {
            var sql = MethodName2Sql.getSql("updateSetAgeAndNameWhereId", "user");
            assertEquals("UPDATE user SET age = ?, name = ? WHERE id = ?", sql.sql());
            assertEquals(SqlType.UPDATE, sql.sqlType());
        }

        @Test
        void update() {
            assertThrows(Exception.class, () -> MethodName2Sql.getSql("update", "user"));
        }
    }

    @Nested
    class CountCases {

        @Test
        void count() {
            var sql = MethodName2Sql.getSql("count", "user");
            assertEquals("SELECT COUNT(*) FROM user", sql.sql());
            assertEquals(SqlType.COUNT, sql.sqlType());
        }

        @Test
        void countByAge() {
            var sql = MethodName2Sql.getSql("countByAge", "user");
            assertEquals("SELECT COUNT(*) FROM user WHERE age = ?", sql.sql());
            assertEquals(SqlType.COUNT, sql.sqlType());
        }

        @Test
        void countByAgeAndGender() {
            var sql = MethodName2Sql.getSql("countByAgeAndGender", "user");
            assertEquals("SELECT COUNT(*) FROM user WHERE age = ? AND gender = ?", sql.sql());
            assertEquals(SqlType.COUNT, sql.sqlType());
        }
    }


    @Nested
    class FindCases {

        @Test
        void get() {
            var sql = MethodName2Sql.getSql("get", "user");
            assertEquals("SELECT * FROM user", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void find() {
            var sql = MethodName2Sql.getSql("find", "user");
            assertEquals("SELECT * FROM user", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findById() {
            var sql = MethodName2Sql.getSql("findById", "user");
            assertEquals("SELECT * FROM user WHERE id = ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void getByDisplayName() {
            var sql = MethodName2Sql.getSql("getByDisplayName", "user");
            assertEquals("SELECT * FROM user WHERE display_name = ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void getWhereUserRegisterTime() {
            var sql = MethodName2Sql.getSql("getWhereUserRegisterTime", "user");
            assertEquals("SELECT * FROM user WHERE user_register_time = ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findOrderByIdLimit() {
            var sql = MethodName2Sql.getSql("findOrderByIdLimit", "user");
            assertEquals("SELECT * FROM user ORDER BY id ASC LIMIT ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }

        @Test
        void findByIdOrderByIdDescLimit() {
            var sql = MethodName2Sql.getSql("findByIdOrderByIdDescLimit", "user");
            assertEquals("SELECT * FROM user WHERE id = ? ORDER BY id DESC LIMIT ?", sql.sql());
            assertEquals(SqlType.SELECT, sql.sqlType());
        }
    }
}
