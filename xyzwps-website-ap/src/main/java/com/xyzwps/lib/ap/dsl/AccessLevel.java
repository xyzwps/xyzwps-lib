package com.xyzwps.lib.ap.dsl;

public enum AccessLevel {
    PUBLIC,
    PROTECTED,
    PRIVATE,
    PACKAGE;

    public String toSourceCode() {
        return switch (this) {
            case PUBLIC -> "public ";
            case PROTECTED -> "protected ";
            case PRIVATE -> "private ";
            case PACKAGE -> " ";
        };
    }
}
