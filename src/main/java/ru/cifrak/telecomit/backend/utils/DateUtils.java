package ru.cifrak.telecomit.backend.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    public static Integer getCurrentYear() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        calendar.setTime(new Date());
        return calendar.get(Calendar.YEAR);
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Integer getYear(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}
