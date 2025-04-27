    package com.example.todolist.Widget;

    import android.content.Context;
    import android.content.Intent;
    import android.view.View;
    import android.widget.RemoteViews;
    import android.widget.RemoteViewsService;

    import com.example.todolist.Database.AppDatabase;
    import com.example.todolist.Model.Enum.TaskStatus;
    import com.example.todolist.Model.Task;
    import com.example.todolist.R;

    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.List;

    public class TaskRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context context;
        private List<Task> taskList = new ArrayList<>();

        public TaskRemoteViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {}

        @Override
        public void onDataSetChanged() {
            List<Task> today = AppDatabase.getInstance(context)
                    .taskDao()
                    .getAllTasksNow();

            taskList.clear();
            for (Task task : today) {
                if (task.getDueDate() != null && task.getDueDate().equals(LocalDate.now())) {
                    taskList.add(task);
                    if (taskList.size() >= 6) break;
                }
            }
        }

        @Override
        public int getCount() {
            return taskList.isEmpty() ? 0 : 6;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_task_widget);

            if (position < taskList.size()) {
                Task task = taskList.get(position);
                views.setTextViewText(R.id.txtTitle, task.getTitle());

                // Gán icon
                views.setImageViewResource(R.id.iconTask, task.getIdIcon());

                // Gán trạng thái hoàn thành
                if (task.getStatus() == TaskStatus.COMPLETED) {
                    views.setViewVisibility(R.id.iconComplete, View.VISIBLE);
                    views.setViewVisibility(R.id.iconNotComplete, View.GONE);
                } else {
                    views.setViewVisibility(R.id.iconComplete, View.GONE);
                    views.setViewVisibility(R.id.iconNotComplete, View.VISIBLE);
                }
            }else {
                views.setTextViewText(R.id.txtTitle, "Chưa có nhiệm vụ");
                views.setImageViewResource(R.id.iconTask, R.drawable.write); // icon mặc định
                views.setViewVisibility(R.id.iconComplete, View.GONE);
                views.setViewVisibility(R.id.iconNotComplete, View.GONE);
            }
//            Intent fillInIntent = new Intent();
//            views.setOnClickFillInIntent(R.id.layout_task_widget, fillInIntent);
            return views;
        }

        @Override public RemoteViews getLoadingView() { return null; }
        @Override public int getViewTypeCount() { return 1; }
        @Override public long getItemId(int position) { return position; }
        @Override public boolean hasStableIds() { return true; }
        @Override public void onDestroy() {taskList.clear(); }
    }