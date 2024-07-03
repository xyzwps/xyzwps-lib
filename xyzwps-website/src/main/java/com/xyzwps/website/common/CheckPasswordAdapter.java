package com.xyzwps.website.common;

import io.avaje.validation.adapter.AbstractConstraintAdapter;
import io.avaje.validation.adapter.ConstraintAdapter;
import io.avaje.validation.adapter.ValidationContext;

import java.util.Arrays;

@ConstraintAdapter(CheckPassword.class)
public final class CheckPasswordAdapter extends AbstractConstraintAdapter<String> {

    public CheckPasswordAdapter(ValidationContext.AdapterCreateRequest request) {
        super(request);
    }

    @Override
    protected boolean isValid(String string) {
        if (string == null) {
            return false;
        }

        final int length = string.length();
        if (length < 8 || length > 32) {
            return false;
        }

        boolean hasDigit = false;
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasSymbol = false;
        for (int i = 0; i < length; i++) {
            var c = string.charAt(i);
            if (!hasDigit && Character.isDigit(c)) {
                hasDigit = true;
            } else if (!hasLower && Character.isLowerCase(c)) {
                hasLower = true;
            } else if (!hasUpper && Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (!hasSymbol && SYMBOLS[c]) {
                hasSymbol = true;
            }
        }

        int kinds = (hasDigit ? 1 : 0) + (hasLower ? 1 : 0) + (hasUpper ? 1 : 0) + (hasSymbol ? 1 : 0);
        return kinds >= 3;
    }

    private static final boolean[] SYMBOLS = new boolean[128];

    static {
        Arrays.fill(SYMBOLS, false);
        String symbols = "~`!@#$%^&*()_+-={}[]|\\:;\"'<,>.?/";
        for (int i = 0; i < symbols.length(); i++) {
            SYMBOLS[symbols.charAt(i)] = true;
        }
    }
}
