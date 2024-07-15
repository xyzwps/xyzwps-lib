package com.xyzwps.lib.ap.util;

import javax.lang.model.type.*;

public record CanonicalName(String packageName, String className) {

    @Override
    public String toString() {
        if (packageName == null || packageName.isEmpty()) {
            return className;
        }
        return packageName + "." + className;
    }

    public static CanonicalName of(String canonicalName) {
        int lastDot = canonicalName.lastIndexOf('.');
        return new CanonicalName(canonicalName.substring(0, lastDot), canonicalName.substring(lastDot + 1));
    }

}
