package com.xyzwps.website.modules.user.payload;

import com.xyzwps.website.common.CheckPassword;
import io.avaje.validation.constraints.NotEmpty;
import io.avaje.validation.constraints.Pattern;
import io.avaje.validation.constraints.Valid;

@Valid
public record LoginBasicPayload(
        @NotEmpty(message = "username is required")
        @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "username must be 4-16 characters long")
        String username,
        @NotEmpty(message = "password is required")
        @CheckPassword
        String password
) {
}
