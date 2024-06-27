package com.xyzwps.lib.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class ColumnPropertyMappers {
    private ColumnPropertyMappers() {
    }

    static final ColumnPropertyMapper<Short> SHORT = new ColumnPropertyMapper<>() {
        @Override
        public Short fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getShort(column);
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, Short property) throws SQLException {
            ps.setShort(index, property);
        }
    };

    static final ColumnPropertyMapper<Integer> INTEGER = new ColumnPropertyMapper<>() {
        @Override
        public Integer fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getInt(column);
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, Integer property) throws SQLException {
            ps.setInt(index, property);
        }
    };

    static final ColumnPropertyMapper<Long> LONG = new ColumnPropertyMapper<>() {
        @Override
        public Long fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getLong(column);
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, Long property) throws SQLException {
            ps.setLong(index, property);
        }
    };

    static final ColumnPropertyMapper<Float> FLOAT = new ColumnPropertyMapper<>() {
        @Override
        public Float fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getFloat(column);
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, Float property) throws SQLException {
            ps.setFloat(index, property);
        }
    };

    static final ColumnPropertyMapper<Double> DOUBLE = new ColumnPropertyMapper<>() {
        @Override
        public Double fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getDouble(column);
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, Double property) throws SQLException {
            ps.setDouble(index, property);
        }
    };

    static final ColumnPropertyMapper<Boolean> BOOLEAN = new ColumnPropertyMapper<>() {
        @Override
        public Boolean fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getBoolean(column);
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, Boolean property) throws SQLException {
            ps.setBoolean(index, property);
        }
    };

    static final ColumnPropertyMapper<String> STRING = new ColumnPropertyMapper<>() {
        @Override
        public String fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getString(column);
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, String property) throws SQLException {
            ps.setString(index, property);
        }
    };

    static final ColumnPropertyMapper<BigInteger> BIG_INTEGER = new ColumnPropertyMapper<>() {
        @Override
        public BigInteger fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getBigDecimal(column).toBigInteger();
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, BigInteger property) throws SQLException {
            ps.setBigDecimal(index, new java.math.BigDecimal(property));
        }
    };

    static final ColumnPropertyMapper<BigDecimal> BIG_DECIMAL = new ColumnPropertyMapper<>() {
        @Override
        public BigDecimal fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getBigDecimal(column);
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, BigDecimal property) throws SQLException {
            ps.setBigDecimal(index, property);
        }
    };

    static final ColumnPropertyMapper<java.sql.Date> SQL_DATE = new ColumnPropertyMapper<>() {
        @Override
        public java.sql.Date fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getDate(column);
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, java.sql.Date property) throws SQLException {
            ps.setDate(index, property);
        }
    };

    static final ColumnPropertyMapper<java.sql.Time> SQL_TIME = new ColumnPropertyMapper<>() {
        @Override
        public java.sql.Time fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getTime(column);
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, java.sql.Time property) throws SQLException {
            ps.setTime(index, property);
        }
    };

    static final ColumnPropertyMapper<java.sql.Timestamp> SQL_TIMESTAMP = new ColumnPropertyMapper<>() {
        @Override
        public java.sql.Timestamp fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getTimestamp(column);
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, java.sql.Timestamp property) throws SQLException {
            ps.setTimestamp(index, property);
        }
    };

    static final ColumnPropertyMapper<java.util.Date> UTIL_DATE = new ColumnPropertyMapper<>() {
        @Override
        public java.util.Date fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getTimestamp(column);
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, java.util.Date property) throws SQLException {
            ps.setTimestamp(index, new java.sql.Timestamp(property.getTime()));
        }
    };

    static final ColumnPropertyMapper<java.time.LocalDate> LOCAL_DATE = new ColumnPropertyMapper<>() {
        @Override
        public java.time.LocalDate fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getDate(column).toLocalDate();
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, java.time.LocalDate property) throws SQLException {
            ps.setDate(index, java.sql.Date.valueOf(property));
        }
    };

    static final ColumnPropertyMapper<java.time.LocalTime> LOCAL_TIME = new ColumnPropertyMapper<>() {
        @Override
        public java.time.LocalTime fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getTime(column).toLocalTime();
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, java.time.LocalTime property) throws SQLException {
            ps.setTime(index, java.sql.Time.valueOf(property));
        }
    };

    static final ColumnPropertyMapper<java.time.LocalDateTime> LOCAL_DATE_TIME = new ColumnPropertyMapper<>() {
        @Override
        public java.time.LocalDateTime fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getTimestamp(column).toLocalDateTime();
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, java.time.LocalDateTime property) throws SQLException {
            ps.setTimestamp(index, java.sql.Timestamp.valueOf(property));
        }
    };

    static final ColumnPropertyMapper<java.time.Instant> INSTANT = new ColumnPropertyMapper<>() {
        @Override
        public java.time.Instant fromColumn(ResultSet rs, String column) throws SQLException {
            return rs.getTimestamp(column).toInstant();
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, java.time.Instant property) throws SQLException {
            ps.setTimestamp(index, java.sql.Timestamp.from(property));
        }
    };

}
