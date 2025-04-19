package com.example.todolist.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateUtils {

    public static String getDayLabel(LocalDate selectedDate) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        if (selectedDate.equals(today)) {
            return "Today";
        } else if (selectedDate.equals(tomorrow)) {
            return "Tomorrow";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM", Locale.ENGLISH);
            return selectedDate.format(formatter);
        }
    }

    public static LocalDate parseLabelToDate(String label) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        if ("Today".equalsIgnoreCase(label)) {
            return today;
        } else if ("Tomorrow".equalsIgnoreCase(label)) {
            return tomorrow;
        } else {
            try {
                int currentYear = today.getYear();
                String labelWithYear = label + " " + currentYear;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH);
                return LocalDate.parse(labelWithYear, formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                return today;
            }
        }
    }
}
