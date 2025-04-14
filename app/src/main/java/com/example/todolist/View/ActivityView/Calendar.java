package com.example.todolist.View.ActivityView;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;
import com.example.todolist.databinding.ActivityCalendarBinding;

public class Calendar extends AppCompatActivity {


    ActivityCalendarBinding  binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        View mview = binding.getRoot();
        setContentView(mview);

        binding.btnBack.setOnClickListener(view->{
            finish();
        });

    }
}