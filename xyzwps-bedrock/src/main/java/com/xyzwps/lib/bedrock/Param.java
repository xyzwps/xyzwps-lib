package com.xyzwps.lib.bedrock;

import java.lang.annotation.*;

/**
 * Annotation for a parameter which would be used as a parameter.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    /**
     * Parameter name.
     *
     * @return Parameter name.
     */
    String value();
}
