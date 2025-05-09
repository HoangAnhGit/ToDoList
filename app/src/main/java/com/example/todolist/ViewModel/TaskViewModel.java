package com.example.todolist.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.todolist.Model.Enum.ReminderSetting;
import com.example.todolist.Model.Task;
import com.example.todolist.Repository.TaskRepository;
import com.example.todolist.Utils.TaskScheduler;

import java.time.LocalDate;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository repository;
    private final LiveData<List<Task>> allTasks;

    private final MutableLiveData<Integer> selectedTagId = new MutableLiveData<>(0);
    private final MutableLiveData<LocalDate> selectedDate = new MutableLiveData<>(null);
    private final LiveData<List<Task>> filteredTasks;

    private final MutableLiveData<String> currentFilter = new MutableLiveData<>("TODAY");


    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
        filteredTasks = Transformations.switchMap(selectedTagId, repository::getTasksByTagId);
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<Task>> getFilteredTasks() {
        return Transformations.switchMap(selectedTagId, tagId -> {
            if (tagId == 0) return repository.getAllTasks();
            else return repository.getTasksByTagId(tagId);
        });
    }

    public void filterTasksByTagId(int tagId) {
        selectedTagId.setValue(tagId);
    }


    public void insert(Task task) {
        new Thread(() -> {
            long id = repository.insertLong(task);
            task.setId((int) id);
            TaskScheduler.scheduleReminder(getApplication(), task);
        }).start();
//        repository.insert(task);
//        TaskScheduler.scheduleReminder(getApplication(), task);
    }

    public void delete(Task task) {
        TaskScheduler.cancelReminder(getApplication(), task.getId());
        repository.delete(task);
    }

    public void update(Task task) {
        TaskScheduler.cancelReminder(getApplication(), task.getId());
        repository.update(task);
        if (task.getReminderSetting() != ReminderSetting.NO_REMINDER) {
            TaskScheduler.scheduleReminder(getApplication(), task);
        }
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public LiveData<List<Task>> getFilteredTaskIndex() {
        return Transformations.switchMap(currentFilter, filter -> {
            switch (filter) {
                case "ALL":
                    return repository.getAllTasks();
                case "WEEK":
                    return repository.getTasksThisWeek();
                case "MONTH":
                    return repository.getTasksThisMonth();
                default:

                    return repository.getTasksToday();
            }
        });
    }

    public void setFilter(String filter) {
        currentFilter.setValue(filter);
    }

    public LiveData<String> getCurrentFilter() {
        return currentFilter;
    }


    //21/4
    public void setSelectedDate(LocalDate date) {
        selectedDate.setValue(date);
    }

    public LiveData<List<Task>> filteredTasksIdTagDate() {
        return Transformations.switchMap(selectedTagId, tagId -> {
            return Transformations.switchMap(selectedDate, date -> {
                if (tagId == 0 && date==null) {
                    return repository.getTasksByDate(LocalDate.now());
                } else if(tagId == 0 && date!=null){
                    return repository.getTasksByDate(date);
                }
                else if (date == null) {
                    return repository.getTasksByTagAndDate(tagId,LocalDate.now());
                } else {
                    return repository.getTasksByTagAndDate(tagId, date);
                }
            });
        });
    }

    //dot
    public LiveData<List<LocalDate>> getUnfinishedTaskDates() {
        return repository.getUnfinishedTaskDates();
    }

    //cập nhập overdue
    public void refreshOverdueTasks() {
        repository.updateOverdueTasks();
    }


    //setting
    public LiveData<Integer> getTotalTaskCount() {
        return Transformations.map(allTasks, tasks -> tasks != null ? tasks.size() : 0);
    }

    public LiveData<Integer> getCompletedTaskCount() {
        return Transformations.map(allTasks, tasks -> {
            if (tasks == null) return 0;
            int count = 0;
            for (Task task : tasks) {
                if (task.getStatus().name().equals("COMPLETED")) count++;
            }
            return count;
        });
    }



}