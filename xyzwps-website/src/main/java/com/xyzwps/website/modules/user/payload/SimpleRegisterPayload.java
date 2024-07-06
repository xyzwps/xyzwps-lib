package com.xyzwps.website.modules.user.payload;

import com.xyzwps.website.common.Regexp;
import io.avaje.validation.constraints.NotEmpty;
import io.avaje.validation.constraints.Pattern;
import io.avaje.validation.constraints.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Valid
public class SimpleRegisterPayload {
    @NotEmpty(message = "phone is required")
    @Pattern(regexp = Regexp.PHONE, message = "phone is invalid")
    private String phone;

    @NotEmpty(message = "vcode is required")
    @Pattern(regexp = Regexp.VCODE, message = "vcode is invalid")
    private String vcode;
}
