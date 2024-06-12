package com.xyzwps.lib.json;

import com.xyzwps.lib.json.element.JsonString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Consumer;

import static com.xyzwps.lib.bedrock.DateTimeUtils.*;

public final class DateTimeConfigurer implements Consumer<JsonMapper> {
    @Override
    public void accept(JsonMapper jm) {

        jm.addToElementConverter(Date.class, (Date date) -> new JsonString(ISO.dateToString(date)));
        jm.addToKeyConverter(Date.class, (Date date) -> ISO.dateToString(date));
        jm.addFromElementConverter(Date.class, (element) -> {
            if (element instanceof JsonString js) {
                return ISO.stringToDate(js.value());
            }
            throw new IllegalArgumentException("Cannot convert to Date from " + element.getClass().getName() + " element.");
        });
        jm.addFromKeyConverter(Date.class, ISO::stringToDate);


        jm.addToElementConverter(LocalDateTime.class, (LocalDateTime dt) -> new JsonString(ISO.localDateTimeToString(dt)));
        jm.addToKeyConverter(LocalDateTime.class, (LocalDateTime dt) -> ISO.localDateTimeToString(dt));
        jm.addFromElementConverter(LocalDateTime.class, (element) -> {
            if (element instanceof JsonString js) {
                return ISO.stringToLocalDateTime(js.value());
            }
            throw new IllegalArgumentException("Cannot convert to LocalDateTime from " + element.getClass().getName() + " element.");
        });
        jm.addFromKeyConverter(LocalDateTime.class, ISO::stringToLocalDateTime);


        jm.addToElementConverter(ZonedDateTime.class, (ZonedDateTime zdt) -> new JsonString(ISO.zonedDateTimeToString(zdt)));
        jm.addToKeyConverter(ZonedDateTime.class, (ZonedDateTime zdt) -> ISO.zonedDateTimeToString(zdt));
        jm.addFromElementConverter(ZonedDateTime.class, (element) -> {
            if (element instanceof JsonString js) {
                return ISO.stringToZonedDateTime(js.value());
            }
            throw new IllegalArgumentException("Cannot convert to ZonedDateTime from " + element.getClass().getName() + " element.");
        });
        jm.addFromKeyConverter(ZonedDateTime.class, ISO::stringToZonedDateTime);


        jm.addToElementConverter(Instant.class, (Instant instant) -> new JsonString(ISO.instantToString(instant)));
        jm.addToKeyConverter(Instant.class, (Instant instant) -> ISO.instantToString(instant));
        jm.addFromElementConverter(Instant.class, (element) -> {
            if (element instanceof JsonString js) {
                return ISO.stringToInstant(js.value());
            }
            throw new IllegalArgumentException("Cannot convert to Instant from " + element.getClass().getName() + " element.");
        });
        jm.addFromKeyConverter(Instant.class, ISO::stringToInstant);

    }

    public static void main(String[] args) {

        var dt = LocalDateTime.now();
        var zdt = ZonedDateTime.of(dt, ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        System.out.println(dtf.format(zdt));
    }


}
