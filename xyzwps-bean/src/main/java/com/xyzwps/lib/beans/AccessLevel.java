package com.xyzwps.lib.beans;

import java.lang.reflect.Modifier;

public enum AccessLevel {
    PUBLIC(true),
    PROTECTED(false),
    PRIVATE(false),
    PACKAGE(false);

    public final boolean readable;

    AccessLevel(boolean readable) {
        this.readable = readable;
    }

    public static final AccessLevel DEFAULT = PACKAGE;

    public static AccessLevel fromModifiers(int modifiers) {
        if (Modifier.isPublic(modifiers)) return PUBLIC;
        if (Modifier.isProtected(modifiers)) return PROTECTED;
        if (Modifier.isPrivate(modifiers)) return PRIVATE;
        return DEFAULT;
    }
}
