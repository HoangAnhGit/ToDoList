package com.example.todolist.Utils;

import android.widget.Switch;

import com.example.todolist.Model.Enum.ReminderSetting;
import com.example.todolist.Model.Enum.RepeatFrequency;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class CoverString {

    public String RepeatToString(RepeatFrequency repeatFrequency, LocalDate time) {

        switch (repeatFrequency) {
            case DAILY:
                return "Repeats every day";

            case WEEKLY:
                String dayOfWeek = time.getDayOfWeek()
                        .getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                return "Repeats every week on " + dayOfWeek;

            case MONTHLY:
                    DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("d", Locale.ENGLISH);
                    return "Repeats every month on " + time.format(monthFormatter);

            case OFF:
            default:
                return "No repeat";
        }
    }

    public String Reminder(ReminderSetting reminderSetting, LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime reminderTime;

        if(time==null){
            return "No Reminder";
        }
        switch (reminderSetting) {
            case FIFTEEN_MINUTES_BEFORE:
                reminderTime = time.minusMinutes(15);
                return "Remind me at: " + reminderTime.format(formatter) + " (15 minutes before)";
            case ONE_HOUR_BEFORE:
                reminderTime = time.minusHours(1);
                return "Remind me at: " + reminderTime.format(formatter) + " (1 hour before)";
            case AT_TIME_OF_DUE:
                return "Remind me at: " + time.format(formatter);
            case NO_REMINDER:
            default:
                return "No Reminder";
        }
    }
}
