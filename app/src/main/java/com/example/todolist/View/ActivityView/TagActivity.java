package com.example.todolist.View.ActivityView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.todolist.Model.Tag;
import com.example.todolist.View.rcv.TagAdapter;
import com.example.todolist.ViewModel.TagViewModel;
import com.example.todolist.ViewModel.TagViewModelFactory;
import com.example.todolist.databinding.ActivityTagBinding;

public class TagActivity extends AppCompatActivity {

    private ActivityTagBinding binding;
    private TagAdapter tagAdapter;

    private Tag tagSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTagBinding.inflate(getLayoutInflater());
        View mView = binding.getRoot();
        setContentView(mView);

        TagViewModelFactory factory = new TagViewModelFactory(getApplication());
        TagViewModel tagViewModel = new ViewModelProvider(this,factory).get(TagViewModel.class);

        tagAdapter = new TagAdapter();
        binding.rcvTag.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvTag.setAdapter(tagAdapter);


        tagViewModel.getAllTags().observe(this, tags -> {
            tagAdapter.setListTag(tags,tag -> {
                tagSelected =tag;
            });
        });

        binding.btnBack.setOnClickListener(view -> {
            int KEY_TAG_RETURN = 98;
            Intent intentReturn = new Intent(this, AddTask.class);
            Bundle bundle = new Bundle();
            if(tagSelected==null){
                tagSelected = tagViewModel.getTagByID(0);
            }
            bundle.putSerializable("tag", tagSelected);
            intentReturn.putExtras(bundle);
            setResult(KEY_TAG_RETURN,intentReturn);
            finish();
        });

        binding.newTag.setOnClickListener(view -> {
            Intent intentNewTag = new Intent(this, NewTag.class);
            startActivity(intentNewTag);
        });

    }
}