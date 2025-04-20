package com.example.todolist.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.todolist.Model.Tag;
import com.example.todolist.Model.Task;
import com.example.todolist.Utils.LocalDateConverter;
import com.example.todolist.Utils.LocalTimeConverter;

@Database(entities = {Task.class, Tag.class}, version = 1)
@TypeConverters({LocalDateConverter.class, LocalTimeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "UpToDo.db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class , DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract TaskDAO taskDao();
    public abstract TagDAO tagDao();
}
