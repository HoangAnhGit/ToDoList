package com.example.todolist.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.todolist.R;

public class FocusModeService extends NotificationListenerService {

    private static boolean isFocusMode = false;

    public static void setFocusMode(boolean active) {
        isFocusMode = active;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (isFocusMode) {
            cancelNotification(sbn.getKey());
            Log.d("FocusMode", "Đã huỷ thông báo từ: " + sbn.getPackageName());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }


}
