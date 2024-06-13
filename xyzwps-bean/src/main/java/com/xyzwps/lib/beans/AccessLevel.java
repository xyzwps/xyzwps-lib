package com.xyzwps.lib.beans;

import java.lang.reflect.Modifier;

/**
 * Access level of a class member.
 */
public enum AccessLevel {
    /**
     * Public access level.
     */
    PUBLIC(true),
    /**
     * Protected access level.
     */
    PROTECTED(false),
    /**
     * Private access level.
     */
    PRIVATE(false),
    /**
     * Package access level.
     */
    PACKAGE(false);

    /**
     * Whether the member is readable.
     */
    public final boolean readable;

    AccessLevel(boolean readable) {
        this.readable = readable;
    }

    /**
     * Default access level.
     */
    public static final AccessLevel DEFAULT = PACKAGE;

    /**
     * Get access level from modifiers.
     *
     * @param modifiers the modifiers of the member.
     * @return the access level of the member.
     */
    public static AccessLevel fromModifiers(int modifiers) {
        if (Modifier.isPublic(modifiers)) return PUBLIC;
        if (Modifier.isProtected(modifiers)) return PROTECTED;
        if (Modifier.isPrivate(modifiers)) return PRIVATE;
        return DEFAULT;
    }
}
