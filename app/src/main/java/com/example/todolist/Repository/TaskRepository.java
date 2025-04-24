package com.example.todolist.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.todolist.Database.AppDatabase;
import com.example.todolist.Database.TaskDAO;
import com.example.todolist.Model.Enum.TaskStatus;
import com.example.todolist.Model.Task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;

public class TaskRepository {
    private final TaskDAO taskDao;
    private final LiveData<List<Task>> allTasks;

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }


    public LiveData<List<Task>> getTasksByTagId(int tagId) {
        return taskDao.getTasksByTagId(tagId);
    }

    public void insert(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.insertTask(task));
    }

    public void delete(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.deleteTask(task));
    }

    public void update(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.updateTask(task));
    }
    public void deleteAll() {
        Executors.newSingleThreadExecutor().execute(taskDao::deleteAll);
    }

    //20/4

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public LiveData<List<Task>> getTasksToday() {
        return taskDao.getTasksForDate(LocalDate.now());
    }

    public LiveData<List<Task>> getTasksThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.with(DayOfWeek.MONDAY);
        LocalDate end = today.with(DayOfWeek.SUNDAY);

        Log.d("TODAY","Start"+ start.format(formatter));
        Log.d("TODAY", "End" +end.format(formatter));
        Log.d("TODAY", today.format(formatter));
        return taskDao.getTasksBetweenDates(start, end);
    }

    public LiveData<List<Task>> getTasksThisMonth() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfMonth(1);
        LocalDate end = today.withDayOfMonth(today.lengthOfMonth());
        Log.d("TODAY", start.format(formatter));
        Log.d("TODAY", end.format(formatter));
        return taskDao.getTasksBetweenDates(start, end);
    }


    public LiveData<List<Task>> getTasksByTagAndDate(int tagId, LocalDate date) {
        return taskDao.getTasksByTagAndDate(tagId, date);
    }

    public LiveData<List<Task>> getTasksByDate(LocalDate date) {
        return taskDao.getTasksByDate( date);
    }

    //dot
    public LiveData<List<LocalDate>> getUnfinishedTaskDates() {
        return taskDao.getUnfinishedTaskDates(TaskStatus.PENDING, TaskStatus.OVERDUE);
    }

    //overdue
    public void updateOverdueTasks() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Task> tasks = taskDao.getAllTasksNow();
            for (Task task : tasks) {
                if (task.isOverdueNow() && task.getStatus() != TaskStatus.OVERDUE) {
                    task.setStatus(TaskStatus.OVERDUE);
                    taskDao.updateTask(task);
                }
            }
        });
    }

    // tính toán task mới
    public Task createRepeatedTask(Task oldTask) {
        Task newTask = new Task();
        newTask.setTitle(oldTask.getTitle());
        newTask.setDescription(oldTask.getDescription());
        newTask.setColorCode(oldTask.getColorCode());
        newTask.setIdIcon(oldTask.getIdIcon());
        newTask.setIdTag(oldTask.getIdTag());
        newTask.setReminderSetting(oldTask.getReminderSetting());
        newTask.setRepeatFrequency(oldTask.getRepeatFrequency());
        newTask.setStatus(TaskStatus.PENDING);

        // Tính toán ngày mới
        LocalDate oldDate = oldTask.getDueDate();
        switch (oldTask.getRepeatFrequency()) {
            case DAILY: newTask.setDueDate(oldDate.plusDays(1)); break;
            case WEEKLY: newTask.setDueDate(oldDate.plusWeeks(1)); break;
            case MONTHLY: newTask.setDueDate(oldDate.plusMonths(1)); break;
            default: newTask.setDueDate(oldDate); break;
        }
        newTask.setDueTime(oldTask.getDueTime());

        return newTask;
    }


}
