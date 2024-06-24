package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.beans.BeanUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.xyzwps.lib.dollar.Dollar.*;

public final class ResultSetToBean {

    private final ConcurrentHashMap<Type, ValueGetter> getters = new ConcurrentHashMap<>();

    interface ValueGetter {
        Object get(ResultSet rs, String column) throws SQLException;
    }

    public ResultSetToBean() {
        getters.put(short.class, ResultSet::getShort);
        getters.put(Short.class, ResultSet::getShort);
        getters.put(int.class, ResultSet::getInt);
        getters.put(Integer.class, ResultSet::getInt);
        getters.put(long.class, ResultSet::getLong);
        getters.put(Long.class, ResultSet::getLong);
        getters.put(float.class, ResultSet::getFloat);
        getters.put(Float.class, ResultSet::getFloat);
        getters.put(double.class, ResultSet::getDouble);
        getters.put(Double.class, ResultSet::getDouble);
        getters.put(boolean.class, ResultSet::getBoolean);
        getters.put(Boolean.class, ResultSet::getBoolean);
        getters.put(BigInteger.class, (rs, col) -> rs.getBigDecimal(col).toBigInteger());
        getters.put(BigDecimal.class, ResultSet::getBigDecimal);
        getters.put(String.class, ResultSet::getString);
        getters.put(java.sql.Date.class, ResultSet::getDate);
        getters.put(java.sql.Time.class, ResultSet::getTime);
        getters.put(java.sql.Timestamp.class, ResultSet::getTimestamp);
        getters.put(java.util.Date.class, ResultSet::getTimestamp);
        getters.put(java.time.LocalDate.class, (rs, col) -> rs.getDate(col).toLocalDate());
        getters.put(java.time.LocalTime.class, (rs, col) -> rs.getTime(col).toLocalTime());
        getters.put(java.time.LocalDateTime.class, (rs, col) -> rs.getTimestamp(col).toLocalDateTime());
        getters.put(java.time.Instant.class, (rs, col) -> rs.getTimestamp(col).toInstant());
    }

    public <T> List<T> toList(ResultSet rs, Class<T> clazz) throws SQLException {
        var list = new ArrayList<T>();
        while (rs.next()) {
            list.add(toBean(rs, clazz));
        }
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
            var columnAnno = prop.findAnnotation(anno -> anno.annotationType().equals(Column.class));
            // TODO: cache column name
            var column = (columnAnno == null) ? $.snakeCase(name) : ((Column) columnAnno).name();

            var getter = getters.get(type);
            if (getter == null) {
                if (type instanceof Class<?> c && c.isEnum()) {
                    var str = rs.getString(column);
                    values.put(name, $.isEmpty(str) ? null : toEnum(c, str));
                } else {
                    throw new IllegalStateException("Unexpected value: " + type);
                }
            } else {
                values.put(name, getter.get(rs, column));
            }
        }

        return bi.create(values);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object toEnum(Class<?> clazz, String str) {
        return Enum.valueOf((Class<? extends Enum>) clazz, str);
    }
}
