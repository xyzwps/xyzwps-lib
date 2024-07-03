package com.xyzwps.website.common;

import io.avaje.validation.constraints.Constraint;

@Constraint
public @interface CheckPassword {
    // TODO: 重新描述密码的要求
    String message() default "password must be 8-32 characters long";

    Class<?>[] groups() default {};
}
