package com.example.todolist.ViewModel;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.todolist.Model.Tag;
import com.example.todolist.R;
import com.example.todolist.Repository.TagRepository;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import io.reactivex.rxjava3.annotations.NonNull;

public class TagViewModel extends AndroidViewModel {

    private final TagRepository repository;
    private final LiveData<List<Tag>> allTags;
    private final Observer<List<Tag>> tagObserver;
    private final AtomicBoolean hasInitializedDefaultTags = new AtomicBoolean(false);



    public TagViewModel(@NonNull Application application) {
        super(application);
        repository = new TagRepository(application);
        allTags = repository.getAllTags();
        tagObserver = tags -> {
            if ((tags == null || tags.isEmpty()) && !hasInitializedDefaultTags.get()) {
                initDefaultTag();
            }
        };
        allTags.observeForever(tagObserver);
    }

    public void initDefaultTag() {
        if (!hasInitializedDefaultTags.compareAndSet(false, true)) {
            return;
        }
        Tag noTag = new Tag("No tag");
        noTag.setUid(1);
        repository.insert(noTag);

        String[] text = getApplication().getResources().getStringArray(R.array.default_tag);
        for (String tagName : text) {
            repository.insert(new Tag(tagName));
        }
    }


    public Tag getTagByID(int id){
        return repository.getTagById(id);
    }
    public LiveData<List<Tag>> getAllTags() {
        return allTags;
    }

    public List<String> getAllTitleTagsList() {
        return repository.getAllTitleTagsList();
    }

    public void insert(Tag tag) {
        repository.insert(tag);
    }

    public void deleteAll(){
        repository.deleteAllTag();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        allTags.removeObserver(tagObserver);
    }

}
