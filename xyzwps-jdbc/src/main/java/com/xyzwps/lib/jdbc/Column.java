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
     * The implementation class of the {@link ColumnPropertyMapper} for the column.
     *
     * @return The implementation class of the {@link ColumnPropertyMapper} for the column.
     */
    Class<?> mapper() default ColumnPropertyMapper.None.class;
}
