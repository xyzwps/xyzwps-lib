package com.xyzwps.lib.express;

public enum SameSite {
    STRICT("Strict"),
    LAX("Lax"),
    NONE("None");

    private final String value;

    SameSite(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}