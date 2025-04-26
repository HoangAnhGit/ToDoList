package com.example.todolist.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.todolist.Model.Enum.ReminderSetting;
import com.example.todolist.Model.Enum.RepeatFrequency;
import com.example.todolist.Model.Task;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TaskScheduler {

    private static final String TAG = "TaskScheduler";

    public static  void scheduleReminder(@NonNull Context context, @NonNull Task task) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null || task.getId() == 0) {
            Log.i(TAG, "Đã hủy báo thức cho task test ");
            return;
        }

        if (task.getDueDate() == null || task.getDueTime() == null ||
                task.getReminderSetting() == null || task.getReminderSetting() == ReminderSetting.NO_REMINDER) {
            Log.i(TAG, "Đã hủy báo thức cho task test 2");
            cancelReminder(context, task.getId());
            return;
        }

        LocalDateTime reminderTime = calculateReminderTime(
                LocalDateTime.of(task.getDueDate(), task.getDueTime()),
                task.getReminderSetting()
        );
        if (reminderTime == null) {
            Log.i(TAG, "Đã hủy báo thức cho task do null ");
            cancelReminder(context, task.getId());
            return;
        }

        long triggerTime = reminderTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (triggerTime <= System.currentTimeMillis()) return;

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction(ReminderReceiver.ACTION_REMIND);
        intent.putExtra(ReminderReceiver.EXTRA_TASK_ID, task.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                task.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }
            Log.i(TAG, "Đã đặt nhắc nhở cho task " + task.getId());
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi đặt báo thức cho task " + task.getId(), e);
        }
    }

    private static long getRepeatInterval(RepeatFrequency repeatFrequency) {
        switch (repeatFrequency) {
            case DAILY: return AlarmManager.INTERVAL_DAY;
            case WEEKLY: return AlarmManager.INTERVAL_DAY * 7;
            case MONTHLY: return AlarmManager.INTERVAL_DAY * 30;
            default: return 0;
        }
    }
    public static  void cancelReminder(@NonNull Context context, int taskId) {
        if (taskId == 0) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction(ReminderReceiver.ACTION_REMIND);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                taskId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        try {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            Log.i(TAG, "Đã hủy báo thức cho task " + taskId);
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi hủy báo thức cho task " + taskId, e);
        }
    }

    private static LocalDateTime calculateReminderTime(LocalDateTime dueDateTime, ReminderSetting setting) {
        switch (setting) {
            case AT_TIME_OF_DUE: return dueDateTime;
            case FIFTEEN_MINUTES_BEFORE: return dueDateTime.minusMinutes(15);
            case ONE_HOUR_BEFORE: return dueDateTime.minusHours(1);
            default: return null;
        }
    }
}
