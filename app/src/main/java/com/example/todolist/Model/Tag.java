package com.example.todolist.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "tag")
public class Tag {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String title;

    public String getTitle() {
        return title;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
