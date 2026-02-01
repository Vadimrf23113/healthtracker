package com.healthtracker.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private DateUtils() {
    }

    public static final DateTimeFormatter UI_DATE =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String format(LocalDate date) {
        if (date == null) return "";
        return UI_DATE.format(date);
    }
}