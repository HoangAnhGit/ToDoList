package com.example.todolist.Calendar;

import java.util.ArrayList;
import java.util.List;

public class CallMonth {
    private String monthName;
    private int monthValue;

    public CallMonth(String monthName, int monthValue) {
        this.monthName = monthName;
        this.monthValue = monthValue;
    }

    public String getMonthName() {
        return monthName;
    }

    public int getMonthValue() {
        return monthValue;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public void setMonthValue(int monthValue) {
        this.monthValue = monthValue;
    }

    public static List<CallMonth> monthsInYear() {
        List<CallMonth> monthList = new ArrayList<>();
        monthList.add(new CallMonth("January", 1));
        monthList.add(new CallMonth("February", 2));
        monthList.add(new CallMonth("March", 3));
        monthList.add(new CallMonth("April", 4));
        monthList.add(new CallMonth("May", 5));
        monthList.add(new CallMonth("June", 6));
        monthList.add(new CallMonth("July", 7));
        monthList.add(new CallMonth("August", 8));
        monthList.add(new CallMonth("September", 9));
        monthList.add(new CallMonth("October", 10));
        monthList.add(new CallMonth("November", 11));
        monthList.add(new CallMonth("December", 12));
        return monthList;
    }

    public static int getPositionByMonthValue(int value) {
        List<CallMonth> monthList = monthsInYear();
        for (int i = 0; i < monthList.size(); i++) {
            if (monthList.get(i).getMonthValue() == value) {
                return i;
            }
        }
        return -1;
    }
}
