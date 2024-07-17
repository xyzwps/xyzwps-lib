package com.xyzwps.lib.openapi;

public enum In {
    QUERY, HEADER, PATH, COOKIE;

    public final String value;

    In() {
        this.value = name().toLowerCase();
    }
}
