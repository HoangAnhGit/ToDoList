package com.example.todolist.ViewModel;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.todolist.Model.Enum.ReminderSetting;
import com.example.todolist.Model.Task;
import com.example.todolist.Repository.TaskRepository;
import com.example.todolist.Utils.TaskReminderReceiver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        repository.insert(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public void update(Task task) {
        repository.update(task);
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

    //reminder
    public void setReminder(Context context, Task task) {
        if (task.getDueTime() == null || task.getReminderSetting() == ReminderSetting.NO_REMINDER)
            return;

        LocalDateTime dateTime = LocalDateTime.of(task.getDueDate(), task.getDueTime());

        switch (task.getReminderSetting()) {
            case AT_TIME_OF_DUE: dateTime = dateTime; break;
            case ONE_HOUR_BEFORE: dateTime = dateTime.minusHours(1); break;
            case FIFTEEN_MINUTES_BEFORE: dateTime = dateTime.minusMinutes(15); break;
            // thêm các trường hợp khác nếu cần
        }

        long triggerTime = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Intent intent = new Intent(context, TaskReminderReceiver.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("taskId", task.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                task.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }

}