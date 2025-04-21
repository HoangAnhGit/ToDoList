package com.example.todolist.Model;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.todolist.Model.Enum.ReminderSetting;
import com.example.todolist.Model.Enum.RepeatFrequency;
import com.example.todolist.Model.Enum.TaskStatus;
import com.example.todolist.R;
import com.example.todolist.Utils.LocalDateConverter;
import com.example.todolist.Utils.LocalTimeConverter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity(tableName = "task")
@TypeConverters({LocalDateConverter.class, LocalTimeConverter.class})
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String colorCode;
    private int idIcon;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private RepeatFrequency repeatFrequency = RepeatFrequency.OFF;
    private ReminderSetting reminderSetting = ReminderSetting.NO_REMINDER;

    private TaskStatus status = TaskStatus.PENDING;


    @ColumnInfo(name = "tag_id")
    private int idTag ;


    public Task() {
    }

    public Task(Context context) {

        this.title ="";
        this.description = "";
        this.idIcon = R.drawable.graduation;
        this.colorCode = String.valueOf(ContextCompat.getColor(context, R.color.item_blue));

        this.dueDate = LocalDate.now();
        this.dueTime = null;
        this.reminderSetting = ReminderSetting.NO_REMINDER;
        this.repeatFrequency = RepeatFrequency.OFF;
        this.idTag = 6 ;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getIdIcon() {
        return idIcon;
    }

    public void setIdIcon(int idIcon) {
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


    public int getIdTag() {
        return idTag;
    }

    public void setIdTag(int idTag) {
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

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", idIcon=" + idIcon +
                ", dueDate=" + dueDate +
                ", dueTime=" + dueTime +
                ", repeatFrequency=" + repeatFrequency +
                ", reminderSetting=" + reminderSetting +
                ", status=" + status +
                ", idTag=" + idTag +
                '}';
    }
}