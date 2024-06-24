package com.xyzwps.lib.jdbc;

import java.lang.annotation.*;

/**
 * An annotation for mapping a field to a column in a database table.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    /**
     * The name of the column in the database table.
     *
     * @return the name of the column in the database table
     */
    String name();

    /**
     * The implementation class of the {@link ValueGetter} for the column.
     *
     * @return The implementation class of the {@link ValueGetter} for the column.
     */
    Class<?> getter() default ValueGetter.None.class;
}
