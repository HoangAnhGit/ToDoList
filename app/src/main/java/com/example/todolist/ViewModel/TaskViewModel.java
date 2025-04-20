package com.example.todolist.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.todolist.Model.Enum.TimeFilter;
import com.example.todolist.Model.Task;
import com.example.todolist.Repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.annotations.NonNull;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository repository;
    private final LiveData<List<Task>> allTasks;

    private final MutableLiveData<Integer> selectedTagId = new MutableLiveData<>();
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
}