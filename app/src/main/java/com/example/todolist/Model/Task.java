package com.example.todolist.Model;



import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.todolist.Model.Enum.ReminderSetting;
import com.example.todolist.Model.Enum.RepeatFrequency;
import com.example.todolist.Model.Enum.TaskStatus;
import com.example.todolist.Utils.LocalDateConverter;
import com.example.todolist.Utils.LocalTimeConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;


@Entity(tableName = "task")
@TypeConverters({LocalDateConverter.class, LocalTimeConverter.class})
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


    private long idTag ;

    public Task() {
    }

    public Task(String title, String description, String colorCode, long idIcon, LocalDate dueDate, long idTag) {
        this.title = title;
        this.description = description;
        this.colorCode = colorCode;
        this.idIcon = idIcon;
        this.dueDate = dueDate;
        this.idTag = idTag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Long getIdIcon() {
        return idIcon;
    }

    public void setIdIcon(Long idIcon) {
        this.idIcon = idIcon;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalTime dueTime) {
        this.dueTime = dueTime;
    }


    public long getIdTag() {
        return idTag;
    }

    public void setIdTag(long idTag) {
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