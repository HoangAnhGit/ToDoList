package com.example.todolist.Model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "icon")
public class Icon {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private int path;


}
