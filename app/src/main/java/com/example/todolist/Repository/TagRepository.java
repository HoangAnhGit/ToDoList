package com.example.todolist.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.todolist.Database.AppDatabase;
import com.example.todolist.Database.TagDAO;
import com.example.todolist.Model.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

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

    public void insert(Tag tag) {
        Executors.newSingleThreadExecutor().execute(() -> tagDao.insertTag(tag));
    }
}
