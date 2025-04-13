package com.example.todolist.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.Model.Tag;

import java.util.List;

@Dao
public interface TagDAO {
    @Insert
    long insertTag(Tag tag);

    @Update
    int updateTag(Tag tag);

    @Delete
    int deleteTag(Tag tag);

    @Query("SELECT * FROM tag ORDER BY title")
    List<Tag> getAllTags();

    @Query("SELECT * FROM tag WHERE id = :id")
    Tag getTagById(long id);

    @Query("SELECT * FROM tag WHERE title = :title LIMIT 1")
    Tag getTagByTitle(String title);
}
