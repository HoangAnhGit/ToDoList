package com.example.todolist.Calendar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CallDay {
    private String dayOfWeek;
    private int day;
    private LocalDate data;

    public CallDay(String dayOfWeek, int day, LocalDate data) {
        this.dayOfWeek = dayOfWeek;
        this.day = day;
        this.data = data;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
    public static List<CallDay> getDaysOfCurrentMonth() {
        List<CallDay> dayList = new ArrayList<>();
        YearMonth yearMonth = YearMonth.now();
        LocalDate today = LocalDate.now();

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            dayList.add(new CallDay(dayOfWeek, date.getDayOfMonth(), date));
        }

        return dayList;
    }

}
