package com.xyzwps.lib.jdbc;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    /**
     * The name of the table in the database.
     *
     * @return the name of the table in the database
     */
    String value();
}
