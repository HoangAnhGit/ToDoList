package com.example.todolist.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todolist.Model.Tag;


@Database(entities = {Tag.class}, version = 1)
public abstract class TagDatabase  extends RoomDatabase {
    private static final String DATABASE_NAME = "tag.db";
    private static TagDatabase instance;

    public static synchronized TagDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),TagDatabase.class , DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract TagDAO tagDAO();
}
