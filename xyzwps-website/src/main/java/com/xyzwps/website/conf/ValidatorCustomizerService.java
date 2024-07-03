package com.xyzwps.website.conf;

import io.avaje.validation.Validator;
import io.avaje.validation.spi.ValidatorCustomizer;

public class ValidatorCustomizerService implements ValidatorCustomizer {
    @Override
    public void customize(Validator.Builder builder) {
        builder.failFast(true);
    }
}
