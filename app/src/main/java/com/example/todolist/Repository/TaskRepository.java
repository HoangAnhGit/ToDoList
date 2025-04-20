package com.example.todolist.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.todolist.Database.AppDatabase;
import com.example.todolist.Database.TaskDAO;
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

}
