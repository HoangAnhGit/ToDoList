package com.example.todolist.ViewModel;


import android.app.Application;
import android.content.Context;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.todolist.Model.Tag;
import com.example.todolist.R;
import com.example.todolist.Repository.TagRepository;
import java.util.List;
import io.reactivex.rxjava3.annotations.NonNull;

public class TagViewModel extends AndroidViewModel {

    private final TagRepository repository;
    private final LiveData<List<Tag>> allTags;

    public TagViewModel(@NonNull Application application) {
        super(application);
        repository = new TagRepository(application);
        allTags = repository.getAllTags();
        allTags.observeForever(tags -> {
            if (tags == null || tags.isEmpty()) {
                initDefaultTag();
            }
        });
    }

    public void initDefaultTag() {
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


}
