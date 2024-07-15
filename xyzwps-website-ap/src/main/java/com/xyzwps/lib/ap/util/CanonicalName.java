package com.xyzwps.lib.ap.util;

public record CanonicalName(String packageName, String className) {

    public static CanonicalName of(String canonicalName) {
        int lastDot = canonicalName.lastIndexOf('.');
        return new CanonicalName(canonicalName.substring(0, lastDot), canonicalName.substring(lastDot + 1));
    }
}
