package ru.cifrak.telecomit.backend;

import org.junit.jupiter.api.Test;
import ru.cifrak.telecomit.backend.utils.Converter;
import ru.cifrak.telecomit.backend.utils.IpReversed;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class IntoMegabytesTest {
    @Test
    public void IpWithoutMaskGetIpTest() {
        assertThat(Converter.megabytes(15411791894L)).isEqualTo("14 697,830");
    }
    @Test
    public void stringTest(){
        Instant begin = Instant.ofEpochSecond(1598893200L);
        Instant end = Instant.ofEpochSecond(1601485200L);
        ZoneId zoneId = ZoneId.of( "Asia/Krasnoyarsk" );
        ZonedDateTime bdt = ZonedDateTime.ofInstant( begin , zoneId );
        ZonedDateTime edt = ZonedDateTime.ofInstant( end , zoneId );
        System.out.println(
            "begin:\t" + bdt +
            "\nend:\t" + edt
        );
    }

    @Test
    public void tTest(){
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Krasnoyarsk"));
        LocalDate previous = now.minus(1, ChronoUnit.DAYS);
        Long till = now.atStartOfDay(ZoneId.of("Asia/Krasnoyarsk")).toInstant().toEpochMilli()/1000;
        Long from = previous.atStartOfDay(ZoneId.of("Asia/Krasnoyarsk")).toInstant().toEpochMilli()/1000;
        System.out.println("now\t: " + now + "\ttill\t:"+till);
        System.out.println("pre\t: " + previous+"\tfrom \t:"+from);

    }
}
