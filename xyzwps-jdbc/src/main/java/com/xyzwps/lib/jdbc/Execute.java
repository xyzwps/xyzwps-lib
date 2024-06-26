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

    /**
     * Whether to batch execute. Batching execute requires that
     * <ol>
     *  <li>method returns void</li>
     *  <li>method accepts a {@link java.util.List}</li>
     * </ol>
     *
     * @return true if batch execute.
     */
    boolean batch() default false;
}
