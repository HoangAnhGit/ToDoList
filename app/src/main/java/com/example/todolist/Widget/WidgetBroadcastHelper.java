package com.example.todolist.Widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.todolist.R;

public class WidgetBroadcastHelper {
    public static void notifyWidgetDataChanged(Context context) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context, WidgetTask.class);
        int[] widgetIds = widgetManager.getAppWidgetIds(widget);
        widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widget_task_grid);
    }
}
