package com.example.todolist.Utils;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

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
