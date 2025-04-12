package com.example.todolist.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todolist.Model.Icon;


@Database(entities = {Icon.class},version = 1)
public abstract class IconDatabase  extends RoomDatabase {
    private static final String DATABASE_NAME = "icon.db";
    private static IconDatabase instance;

    public static synchronized IconDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),IconDatabase.class , DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract IconDAO iconDAO();
}
