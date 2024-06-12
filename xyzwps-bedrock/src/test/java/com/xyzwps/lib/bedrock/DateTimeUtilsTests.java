package com.xyzwps.lib.bedrock;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.bedrock.DateTimeUtils.*;

class DateTimeUtilsTests {

    @Nested
    class RFC1123Tests {
        @Test
        void testInstant() {
            var instant = Instant.ofEpochMilli(1614585802023L);
            assertEquals("Mon, 1 Mar 2021 08:03:22 GMT", RFC1123.instantToString(instant));
        }
    }

    @Nested
    class ISOTests {

        @Test
        void testDate() {
            var c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
            c.set(2021, Calendar.MARCH, 1, 16, 3, 22);
            c.set(Calendar.MILLISECOND, 23);
            var date = c.getTime();
            assertEquals("2021-03-01T08:03:22.023Z", ISO.dateToString(date));
            assertEquals(date, ISO.stringToDate("2021-03-01T08:03:22.023Z"));
        }

        @Test
        void testInstant() {
            var instant = Instant.ofEpochMilli(1614585802023L);
            assertEquals("2021-03-01T08:03:22.023Z", ISO.instantToString(instant));
            assertEquals(instant, ISO.stringToInstant("2021-03-01T08:03:22.023Z"));
        }

        @Test
        void testLocalDateTime() {
            var dt = LocalDateTime.of(2021, 3, 1, 16, 3, 22, 23_000_000);
            assertEquals("2021-03-01T08:03:22.023Z", ISO.localDateTimeToString(dt));
            assertEquals(dt, ISO.stringToLocalDateTime("2021-03-01T08:03:22.023Z"));
        }

        @Test
        void testZonedDateTime() {
            var dt = ZonedDateTime.of(LocalDateTime.of(2021, 3, 1, 16, 3, 22, 23_000_000), ZoneId.systemDefault());
            assertEquals("2021-03-01T08:03:22.023Z", ISO.zonedDateTimeToString(dt));
            assertEquals(dt, ISO.stringToZonedDateTime("2021-03-01T08:03:22.023Z"));
        }

    }
}
