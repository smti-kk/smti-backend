package ru.cifrak.telecomit.backend.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Converter {
    public static String megabytes(long bytes) {
        Locale russian = new Locale("ru");
        return String.format(russian, "%,.3f", (bytes / (double) (1024 * 1024)));
    }
    public static String simpleDate(Instant instant){
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDate( FormatStyle.SHORT)
                        .withLocale( new Locale("ru") )
                        .withZone( ZoneId.of("Asia/Krasnoyarsk") );
        return formatter.format(instant);
    }
}
