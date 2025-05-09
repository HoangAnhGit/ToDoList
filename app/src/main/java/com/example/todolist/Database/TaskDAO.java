package com.example.todolist.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.*;

import com.example.todolist.Model.Enum.TaskStatus;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.TaskWithTag;

import java.time.LocalDate;
import java.util.List;


@Dao
public interface TaskDAO {
    @Insert
    long insertTask(Task task);

    @Update
    int updateTask(Task task);

    @Delete
    int deleteTask(Task task);

    @Query("SELECT * FROM task WHERE id = :id")
    Task getTaskById(long id);

    @Query("SELECT * FROM task ORDER BY dueDate")
    LiveData<List<Task>> getAllTasks();
    @Query("SELECT * FROM task WHERE status = :status")
    List<Task> getTasksByStatus(TaskStatus status);

    @Query("UPDATE task SET status = :status WHERE id = :id")
    void updateStatus(long id, TaskStatus status);

    @Query("DELETE FROM task WHERE status = :status")
    void deleteTasksByStatus(TaskStatus status);

    @Transaction
    @Query("SELECT * FROM task")
    LiveData<List<TaskWithTag>> getTasksWithTag();

    @Query("SELECT * FROM task WHERE tag_id = :tagId")
    LiveData<List<Task>> getTasksByTagId(int tagId);


    @Query("DELETE FROM task")
    void deleteAll();

    // Các filter dùng trong Repository
    @Query("SELECT * FROM task WHERE dueDate = :date")
    LiveData<List<Task>> getTasksForDate(LocalDate date);

    @Query("SELECT * FROM task WHERE dueDate BETWEEN :start AND :end")
    LiveData<List<Task>> getTasksBetweenDates(LocalDate start, LocalDate end);

    @Query("SELECT * FROM task WHERE tag_id = :tagId AND dueDate = :date")
    LiveData<List<Task>> getTasksByTagAndDate(int tagId, LocalDate date);

    @Query("SELECT * FROM task WHERE  dueDate = :date")
    LiveData<List<Task>> getTasksByDate( LocalDate date);

    @Query("SELECT DISTINCT dueDate FROM Task WHERE status = :pending OR status = :overdue")
    LiveData<List<LocalDate>> getUnfinishedTaskDates(TaskStatus pending, TaskStatus overdue);

    //overdue
    @Query("SELECT * FROM Task")
    List<Task> getAllTasksNow();
}
