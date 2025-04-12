package com.example.todolist.Database;

import androidx.room.Insert;
import androidx.room.Query;

import com.example.todolist.Model.Icon;

import java.util.List;

public interface IconDAO {

    @Insert
    long insertIcon(Icon icon);

    @Query("SELECT * FROM icon")
    List<Icon> getAllIcons();

    @Query("SELECT * FROM icon WHERE id = :id")
    Icon getIconById(long id);
}
