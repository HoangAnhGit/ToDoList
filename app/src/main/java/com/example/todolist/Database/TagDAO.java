package com.example.todolist.Database;

import androidx.lifecycle.LiveData;
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

    @Query("DELETE FROM tag")
    void deleteAll();

    @Query("DELETE FROM tag")
    void deleteAllTags();

    @Query("SELECT * FROM tag ")
    LiveData<List<Tag>> getAllTags();

    @Query("SELECT title FROM tag ")
    List<String> getAllTitleTagsList();

    @Query("SELECT * FROM tag WHERE uid = :id")
    Tag getTagById(int id);

    @Query("SELECT * FROM tag WHERE title = :title LIMIT 1")
    Tag getTagByTitle(String title);
}
