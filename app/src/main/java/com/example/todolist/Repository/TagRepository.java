package com.example.todolist.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.todolist.Database.AppDatabase;
import com.example.todolist.Database.TagDAO;
import com.example.todolist.Model.Tag;


import java.util.List;

public class TagRepository {
    private final TagDAO tagDao;

    public TagRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        tagDao = db.tagDao();
    }



    public Tag getTagById(int id){
        return tagDao.getTagById(id);
    }
    public LiveData<List<Tag>> getAllTags() {
        return tagDao.getAllTags();
    }
    public List<String> getAllTitleTagsList() {
        return tagDao.getAllTitleTagsList();
    }

    public void insert(Tag tag) {
        AppDatabase.databaseExecutor().execute(() -> tagDao.insertTag(tag));
    }

    public void deleteAllTag(){
        AppDatabase.databaseExecutor().execute(tagDao::deleteAll);
    }
}
