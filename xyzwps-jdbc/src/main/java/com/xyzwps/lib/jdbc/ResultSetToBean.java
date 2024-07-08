package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.beans.BeanUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.xyzwps.lib.dollar.Dollar.*;

public final class ResultSetToBean {

    private final ConcurrentHashMap<Type, ColumnPropertyMapper<?>> getters = new ConcurrentHashMap<>();

    public ResultSetToBean() {
        getters.put(short.class, ColumnPropertyMappers.SHORT);
        getters.put(Short.class, ColumnPropertyMappers.SHORT);
        getters.put(int.class, ColumnPropertyMappers.INTEGER);
        getters.put(Integer.class, ColumnPropertyMappers.INTEGER);
        getters.put(long.class, ColumnPropertyMappers.LONG);
        getters.put(Long.class, ColumnPropertyMappers.LONG);
        getters.put(float.class, ColumnPropertyMappers.FLOAT);
        getters.put(Float.class, ColumnPropertyMappers.FLOAT);
        getters.put(double.class, ColumnPropertyMappers.DOUBLE);
        getters.put(Double.class, ColumnPropertyMappers.DOUBLE);
        getters.put(boolean.class, ColumnPropertyMappers.BOOLEAN);
        getters.put(Boolean.class, ColumnPropertyMappers.BOOLEAN);
        getters.put(BigInteger.class, ColumnPropertyMappers.BIG_INTEGER);
        getters.put(BigDecimal.class, ColumnPropertyMappers.BIG_DECIMAL);
        getters.put(String.class, ColumnPropertyMappers.STRING);
        getters.put(java.sql.Date.class, ColumnPropertyMappers.SQL_DATE);
        getters.put(java.sql.Time.class, ColumnPropertyMappers.SQL_TIME);
        getters.put(java.sql.Timestamp.class, ColumnPropertyMappers.SQL_TIMESTAMP);
        getters.put(java.util.Date.class, ColumnPropertyMappers.UTIL_DATE);
        getters.put(java.time.LocalDate.class, ColumnPropertyMappers.LOCAL_DATE);
        getters.put(java.time.LocalTime.class, ColumnPropertyMappers.LOCAL_TIME);
        getters.put(java.time.LocalDateTime.class, ColumnPropertyMappers.LOCAL_DATE_TIME);
        getters.put(java.time.Instant.class, ColumnPropertyMappers.INSTANT);
    }

    @SuppressWarnings("unchecked")
    public <T> ArrayList<T> toList(ResultSet rs, Class<T> clazz) throws SQLException {
        var list = new ArrayList<T>();
        if (clazz == String.class) {
            while (rs.next()) list.add((T) rs.getString(1));
            return list;
        }

        if (clazz == Short.class || clazz == short.class) {
            while (rs.next()) list.add((T) Short.valueOf(rs.getShort(1)));
            return list;
        }
        if (clazz == Integer.class || clazz == int.class) {
            while (rs.next()) list.add((T) Integer.valueOf(rs.getInt(1)));
            return list;
        }

        if (clazz == Long.class || clazz == long.class) {
            while (rs.next()) list.add((T) Long.valueOf(rs.getLong(1)));
            return list;
        }

        if (clazz == Double.class || clazz == double.class) {
            while (rs.next()) list.add((T) Double.valueOf(rs.getDouble(1)));
            return list;
        }

        if (clazz == Float.class || clazz == float.class) {
            while (rs.next()) list.add((T) Float.valueOf(rs.getFloat(1)));
            return list;
        }

        if (clazz == Boolean.class || clazz == boolean.class) {
            while (rs.next()) list.add((T) Boolean.valueOf(rs.getBoolean(1)));
            return list;
        }

        while (rs.next()) list.add(toBean(rs, clazz));
        return list;
    }

    public <T> T toBean(ResultSet rs, Class<T> clazz) throws SQLException {
        var bi = BeanUtils.getBeanInfoFromClass(clazz);
        var values = new HashMap<String, Object>();

        var isClass = !bi.isRecord();
        for (var prop : bi.getBeanProperties()) {
            if (isClass && !prop.writable()) {
                continue;
            }

            var type = prop.type();
            if (type == null) {
                throw new IllegalStateException("Unexpected value: " + prop.type());
            }
            if (type == void.class) {
                throw new IllegalStateException("Unexpected value: " + prop.type());
            }

            var name = prop.name();
            var anno = prop.getAnnotation(Column.class);

            var column = $.snakeCase(name);
            ColumnPropertyMapper<?> mapper = anno != null && anno.mapper() != ColumnPropertyMapper.None.class
                    ? getFromClass(anno.mapper())
                    : getters.get(type);
            if (mapper == null) {
                if (type instanceof Class<?> c && c.isEnum()) {
                    var str = rs.getString(column);
                    values.put(name, $.isEmpty(str) ? null : toEnum(c, str));
                } else {
                    throw new DbException("Unexpected value: " + type);
                }
            } else {
                values.put(name, mapper.fromColumn(rs, column));
            }
        }

        return bi.create(values);
    }

    private static ColumnPropertyMapper<?> getFromClass(Class<?> clazz) {
        var inst = InstanceUtils.createInstanceFromDefaultConstructor(clazz);
        if (inst instanceof ColumnPropertyMapper<?> mapper) {
            return mapper;
        }

        throw new DbException("The class " + clazz.getCanonicalName() + " is not a ColumnPropertyMapper.");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object toEnum(Class<?> clazz, String str) {
        return Enum.valueOf((Class<? extends Enum>) clazz, str);
    }
}
