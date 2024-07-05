package com.xyzwps.lib.jdbc.method2sql;

public record SqlInfo(String sql, BooleanList placeholderIsIn, SqlType sqlType) {
}
