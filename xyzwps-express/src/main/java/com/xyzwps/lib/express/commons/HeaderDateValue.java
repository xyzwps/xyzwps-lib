package com.xyzwps.lib.express.commons;

import java.time.Instant;

import static com.xyzwps.lib.bedrock.DateTimeUtils.RFC1123;

public final class HeaderDateValue {
    public static String get() {
        return RFC1123.instantToString(Instant.now());
    }
}
