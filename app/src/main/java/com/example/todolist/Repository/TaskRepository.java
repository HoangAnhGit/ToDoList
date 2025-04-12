package com.example.todolist.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.todolist.Database.TaskDAO;
import com.example.todolist.Database.TaskDatabase;
import com.example.todolist.Model.Task;

import java.util.List;
import java.util.concurrent.Executors;

public class TaskRepository {
        private final TaskDAO taskDao;
        private final LiveData<List<Task>> allTasks;

        public TaskRepository(Application application) {
            TaskDatabase db = TaskDatabase.getInstance(application);
            taskDao = db.taskDAO();
            allTasks = taskDao.getAllTasks();
        }

        public LiveData<List<Task>> getAllTasks() {
            return allTasks;
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

}
