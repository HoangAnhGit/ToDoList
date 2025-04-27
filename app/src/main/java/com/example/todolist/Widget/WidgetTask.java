package com.example.todolist.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.todolist.R;
import com.example.todolist.View.MainActivity;

public class WidgetTask extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_task);

            // Adapter
            Intent serviceIntent = new Intent(context, TaskWidgetService.class);
            views.setRemoteAdapter(R.id.widget_task_grid, serviceIntent);

            // Empty view
            views.setEmptyView(R.id.widget_task_grid, R.id.widget_empty_view);

// mở MainActivity
            Intent clickIntent = new Intent(context, MainActivity.class);
            PendingIntent clickPendingIntent = PendingIntent.getActivity(
                    context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            views.setPendingIntentTemplate(R.id.widget_task_grid, clickPendingIntent);

            appWidgetManager.updateAppWidget(widgetId, views);
        }
    }
}