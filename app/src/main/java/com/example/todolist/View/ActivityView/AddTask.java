package com.example.todolist.View.ActivityView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.todolist.R;
import com.example.todolist.databinding.ActivityAddTaskBinding;

public class AddTask extends AppCompatActivity {

    ActivityAddTaskBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        View mview = binding.getRoot();
        setContentView(mview);

        binding.btnBack.setOnClickListener(view->{
            finish();
        });

        binding.btnCreate.setOnClickListener(view->{
            finish();
        });

        binding.pickDate.setOnClickListener(view->{
            Intent intent = new Intent(this, Calendar.class);
            startActivity(intent);
        });

        binding.pickReminder.setOnClickListener(view->{
            Intent intent = new Intent(this, Reminder.class);
            startActivity(intent);
        });


    }
}