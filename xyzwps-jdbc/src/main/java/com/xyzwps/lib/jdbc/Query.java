package com.xyzwps.lib.jdbc;

import java.lang.annotation.*;

/**
 * Annotation for query method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Query {
    /**
     * SQL statement.
     *
     * @return SQL statement.
     */
    String sql();
}
