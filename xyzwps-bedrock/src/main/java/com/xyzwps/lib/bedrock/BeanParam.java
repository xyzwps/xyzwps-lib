package com.xyzwps.lib.bedrock;

import java.lang.annotation.*;

/**
 * Annotation for a parameter which would be used as a bean parameter.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeanParam {
    /**
     * Bean name.
     *
     * @return Bean name.
     */
    String value();
}
