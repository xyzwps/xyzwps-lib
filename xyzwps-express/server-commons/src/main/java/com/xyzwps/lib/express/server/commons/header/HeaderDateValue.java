package com.xyzwps.lib.express.server.commons.header;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class HeaderDateValue {
    public static String get() {
        return Instant.now().atZone(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
