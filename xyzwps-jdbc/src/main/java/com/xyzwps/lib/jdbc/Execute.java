package com.xyzwps.lib.jdbc;

import java.lang.annotation.*;

/**
 * Annotation for execute method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Execute {
    /**
     * SQL statement. For example, INSERT, UPDATE, DELETE.
     *
     * @return SQL statement.
     */
    String sql();

    boolean returnGeneratedKeys() default false;
}
