package com.example.todolist.Utils;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;


    @TypeConverter
    @Nullable
    public static LocalDate fromString(String value) {
        return value == null ? null : LocalDate.parse(value, formatter);
    }

    @TypeConverter
    @Nullable
    public static String dateToString(LocalDate date) {
        return date == null ? null : date.format(formatter);
    }
}
