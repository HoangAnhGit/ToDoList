package com.example.todolist.Utils;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class CalendarUtils {

    public static List<LocalDate> getDaysInMonth(YearMonth yearMonth) {
        List<LocalDate> days = new ArrayList<>();
        int daysInMonth = yearMonth.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(yearMonth.atDay(i));
        }
        return days;
    }
}
