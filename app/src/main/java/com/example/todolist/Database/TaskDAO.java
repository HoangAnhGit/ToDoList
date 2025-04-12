package com.example.todolist.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.*;

import com.example.todolist.Model.Enum.TaskStatus;
import com.example.todolist.Model.Task;

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

    @Query("SELECT * FROM task WHERE idTag = :tagId ORDER BY dueDate")
    List<Task> getTasksByTag(long tagId);

    @Query("SELECT * FROM task ORDER BY dueDate")
    LiveData<List<Task>> getAllTasks();
    @Query("SELECT * FROM task WHERE status = :status")
    List<Task> getTasksByStatus(TaskStatus status);

    @Query("UPDATE task SET status = :status WHERE id = :id")
    void updateStatus(long id, TaskStatus status);

    @Query("DELETE FROM task WHERE status = :status")
    void deleteTasksByStatus(TaskStatus status);
}
