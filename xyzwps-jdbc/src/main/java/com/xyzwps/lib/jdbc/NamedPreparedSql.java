package com.xyzwps.lib.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * NamedPreparedSql represents a prepared sql with named parameters.
 *
 * @param sql   prepared sql
 * @param names named parameters list
 */
record NamedPreparedSql(String sql, List<String> names) {
    public NamedPreparedSql {
        if (sql == null || sql.isEmpty()) {
            throw new IllegalArgumentException("sql must not be empty");
        }
        if (names == null) {
            throw new IllegalArgumentException("names must not be null");
        }
    }

    private static final Pattern NAMED_PARAMS = Pattern.compile("(:([a-zA-Z_][a-zA-Z0-9_]*\\.)?[a-zA-Z_][a-zA-Z0-9_]*)");

    /**
     * Create a NamedPreparedSql object. For example:
     * <pre>
     *  select * from where id = :id and name = :u.name
     * </pre>
     * will be converted to
     * <pre>
     *  new NamedPreparedSql(
     *      "select * from where id = ? and name = ?",
     *      List.of("id", "u.name")
     *  )
     * </pre>
     *
     * @param sql named-parameter sql
     * @return NamedPreparedSql object
     */
    public static NamedPreparedSql create(String sql) {
        var params = new ArrayList<String>();
        var matcher = NAMED_PARAMS.matcher(sql);
        var psql = new StringBuilder();
        int start = 0;
        while (matcher.find()) {
            psql.append(sql, start, matcher.start()).append("?");
            start = matcher.end();
            params.add(matcher.group().substring(1));
        }
        if (start == 0) {
            return new NamedPreparedSql(sql, List.of());
        } else {
            psql.append(sql, start, sql.length());
            return new NamedPreparedSql(psql.toString(), params);
        }
    }

}
