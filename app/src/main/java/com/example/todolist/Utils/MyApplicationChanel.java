package com.example.todolist.Utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplicationChanel extends Application {
    public static final String CHANNEL_ID = "focus_mode_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Focus Mode Alerts";
            String description = "Thông báo khi phiên Focus Mode kết thúc";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }
}
