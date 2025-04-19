package com.example.todolist.View.ActivityView;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolist.Model.Tag;
import com.example.todolist.ViewModel.TagViewModel;
import com.example.todolist.databinding.ActivityNewTagBinding;


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
            String titleTask = binding.titleTag.getText().toString();
            if(!titleTask.isEmpty()){
                tagViewModel.insert(new Tag(titleTask));
                Toast.makeText(this, "Create tag complete", Toast.LENGTH_SHORT).show();
            }

            finish();
        });

    }
}