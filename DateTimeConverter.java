package org;

import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {

    public static String printZonedDateTime(ZonedDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public static String printLocalDate(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static String printOffsetTime(OffsetTime time) {
        return time.format(DateTimeFormatter.ISO_OFFSET_TIME);
    }

}
