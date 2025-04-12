package com.example.todolist.Model;



import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.todolist.Model.Enum.ReminderSetting;
import com.example.todolist.Model.Enum.RepeatFrequency;
import com.example.todolist.Model.Enum.TaskStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;


@Entity(tableName = "task")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private Long id;


    private String title;
    private String description;
    private String colorCode;
    private Long idIcon;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private RepeatFrequency repeatFrequency = RepeatFrequency.OFF;
    private ReminderSetting reminderSetting = ReminderSetting.NO_REMINDER;

    private TaskStatus status = TaskStatus.PENDING;

    private LocalDateTime createdAt;


    private long idTag ;

    public Task() {
    }

    public Task(String title, String description, String colorCode, long idIcon, LocalDate dueDate, LocalDateTime createdAt, long idTag) {
        this.title = title;
        this.description = description;
        this.colorCode = colorCode;
        this.idIcon = idIcon;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.idTag = idTag;
    }

    public RepeatFrequency getRepeatFrequency() {
        return repeatFrequency;
    }

    public void setRepeatFrequency(RepeatFrequency repeatFrequency) {
        this.repeatFrequency = repeatFrequency;
    }

    public ReminderSetting getReminderSetting() {
        return reminderSetting;
    }

    public void setReminderSetting(ReminderSetting reminderSetting) {
        this.reminderSetting = reminderSetting;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}