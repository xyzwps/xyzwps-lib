package com.xyzwps.lib.ap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Dao {
    /**
     * {@link com.xyzwps.lib.jdbc.Database} bean name.
     *
     * @return {@link com.xyzwps.lib.jdbc.Database} bean name.
     */
    String value() default "";
}
