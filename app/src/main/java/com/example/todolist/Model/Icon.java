package com.example.todolist.Model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "icon")
public class Icon {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private int path;

    public Icon() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }
}
