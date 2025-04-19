package com.example.todolist.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    private static final DateTimeFormatter F =
            DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());

    public static String toLabel(LocalTime t) {
        return (t == null) ? "" : t.format(F);
    }

    public static LocalTime fromLabel(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return LocalTime.parse(s, F);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
