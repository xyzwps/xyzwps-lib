package com.xyzwps.lib.bedrock;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * A utility class for date and time.
 */
public final class DateTimeUtils {

    /**
     * A utility class for RFC 1123 date and time.
     */
    public static final class RFC1123 {

        public static String instantToString(Instant instant) {
            return instant.atZone(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME);
        }

        private RFC1123() throws IllegalAccessException {
            throw new IllegalAccessException("??");
        }
    }

    /**
     * A utility class for ISO 8061 date and time.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date/toISOString">MDN: <code>Date.prototype.toISOString()</code></a>
     */
    public static final class ISO {

        private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

        private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();

        private static final DateTimeFormatter UTC_DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        private static final Pattern UTC_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$");

        /**
         * Convert an {@link Instant} to a string.
         *
         * @param instant the instant to convert
         * @return the string representation of the instant
         */
        public static String instantToString(Instant instant) {
            return UTC_DTF.format(instant.atZone(UTC_ZONE));
        }

        /**
         * Convert a string to an {@link Instant}.
         *
         * @param str the string to convert
         * @return the instant representation of the string
         */
        public static Instant stringToInstant(String str) {
            return LocalDateTime.parse(str, UTC_DTF).atZone(UTC_ZONE).toInstant();
        }

        /**
         * Convert a {@link LocalDateTime} to a string.
         *
         * @param dt the local date time to convert
         * @return the string representation of the local date time
         */
        public static String localDateTimeToString(LocalDateTime dt) {
            var zdt = ZonedDateTime.of(dt, SYSTEM_ZONE).withZoneSameInstant(UTC_ZONE);
            return zonedDateTimeToString(zdt);
        }

        /**
         * Convert a string to a {@link LocalDateTime}.
         *
         * @param str the string to convert
         * @return the local date time representation of the string
         */
        public static LocalDateTime stringToLocalDateTime(String str) {
            return LocalDateTime.parse(str, UTC_DTF)
                    .atZone(UTC_ZONE)
                    .withZoneSameInstant(SYSTEM_ZONE)
                    .toLocalDateTime();
        }


        /**
         * Convert a {@link ZonedDateTime} to a string.
         *
         * @param zdt the zoned date time to convert
         * @return the string representation of the zoned date time
         */
        public static String zonedDateTimeToString(ZonedDateTime zdt) {
            return zdt.withZoneSameInstant(UTC_ZONE).format(UTC_DTF);
        }

        /**
         * Convert a string to a {@link ZonedDateTime}.
         *
         * @param str the string to convert
         * @return the zoned date time representation of the string
         */
        public static ZonedDateTime stringToZonedDateTime(String str) {
            return LocalDateTime.parse(str, UTC_DTF)
                    .atZone(UTC_ZONE)
                    .withZoneSameInstant(SYSTEM_ZONE);
        }

        /**
         * Convert a {@link Date} to a string.
         *
         * @param date the date to convert
         * @return the string representation of the date
         */
        public static String dateToString(Date date) {
            var c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            c.setTime(date);
            return String.format("%d-%02d-%02dT%02d:%02d:%02d.%03dZ",
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH),
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND));
        }

        /**
         * Convert a string to a {@link Date}.
         *
         * @param str the string to convert
         * @return the date representation of the string
         */
        public static Date stringToDate(String str) {
            if (str == null || !UTC_PATTERN.matcher(str).matches()) {
                throw new IllegalArgumentException("Invalid UTC format string");
            }

            var year = Integer.parseInt(str.substring(0, 4));
            var month = Integer.parseInt(str.substring(5, 7));
            var day = Integer.parseInt(str.substring(8, 10));
            var hour = Integer.parseInt(str.substring(11, 13));
            var minute = Integer.parseInt(str.substring(14, 16));
            var second = Integer.parseInt(str.substring(17, 19));
            var millisecond = Integer.parseInt(str.substring(20, 23));

            var c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            //noinspection MagicConstant
            c.set(year, month - 1, day, hour, minute, second);
            c.set(Calendar.MILLISECOND, millisecond);
            return c.getTime();
        }


        private ISO() throws IllegalAccessException {
            throw new IllegalAccessException("??");
        }
    }

    private DateTimeUtils() throws IllegalAccessException {
        throw new IllegalAccessException("??");
    }
}
