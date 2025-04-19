package com.example.todolist.View.ActivityView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolist.Model.Tag;
import com.example.todolist.R;
import com.example.todolist.ViewModel.TagViewModel;
import com.example.todolist.databinding.ActivityNewTagBinding;

import java.util.Objects;

public class NewTag extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityNewTagBinding binding = ActivityNewTagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        TagViewModel tagViewModel = new ViewModelProvider(this).get(TagViewModel.class);
        binding.createTag.setOnClickListener(view -> {
            tagViewModel.insert(new Tag(Objects.requireNonNull(binding.titleTag.getText()).toString()));
            Toast.makeText(this, "Create tag complete", Toast.LENGTH_SHORT).show();
            finish();
        });

    }
}