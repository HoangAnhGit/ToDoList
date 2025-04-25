package com.example.todolist.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.example.todolist.R;

public class ReminderReceiver extends BroadcastReceiver {

    public static final String ACTION_REMIND = "com.example.todolist.ACTION_REMIND";
    public static final String EXTRA_TASK_ID = "extra_task_id";

    private static final String CHANNEL_ID = "task_reminder_channel";
    private static final String CHANNEL_NAME = "Task Reminder";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && ACTION_REMIND.equals(intent.getAction())) {
            int taskId = intent.getIntExtra(EXTRA_TASK_ID, -1);
            Log.d("ReminderReceiver", "Received reminder for task ID: " + taskId);

            showNotification(context, taskId);
        }
    }

    private void showNotification(Context context, int taskId) {
        createNotificationChannel(context);

        // Khi bấm vào thông báo sẽ mở app
        Intent intent = new Intent(context, com.example.todolist.View.MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                taskId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Task Reminder")
                .setContentText("You have a task to complete!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(taskId, builder.build());
    }

    private void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription("Channel for task reminders");

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }
}
