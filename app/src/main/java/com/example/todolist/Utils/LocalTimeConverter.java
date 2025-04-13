package com.example.todolist.Utils;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;

    @TypeConverter
    @Nullable
    public static LocalTime fromString(String value) {
        return value == null ? null : LocalTime.parse(value, formatter);
    }

    @TypeConverter
    @Nullable
    public static String timeToString(LocalTime time) {
        return time == null ? null : time.format(formatter);
    }
}
