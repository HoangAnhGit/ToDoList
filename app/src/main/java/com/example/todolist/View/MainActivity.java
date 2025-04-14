package com.example.todolist.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.todolist.R;
import com.example.todolist.View.ActivityView.AddTask;
import com.example.todolist.View.FragmentView.CalendarFragment;
import com.example.todolist.View.FragmentView.FocusFragment;
import com.example.todolist.View.FragmentView.FragmentIndex;
import com.example.todolist.View.FragmentView.ProfileFragment;
import com.example.todolist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    final static int INDEX_FRAGMENT = R.id.index;
    final static int CALENDAR_FRAGMENT = R.id.calendar;
    final static int FOCUS_FRAGMENT = R.id.focus;
    final static int FR_FRAGMENT = R.id.profile;


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View mview = binding.getRoot();
        setContentView(mview);


        transactionFragment();

        binding.floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddTask.class);
            startActivity(intent);
        });



    }

    private void transactionFragment(){
        replaceFragment(new FragmentIndex());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == INDEX_FRAGMENT) {
                replaceFragment(new FragmentIndex());
            }
            if (itemId == FOCUS_FRAGMENT) {
                replaceFragment(new FocusFragment());
            }
            if (itemId == CALENDAR_FRAGMENT) {
                replaceFragment(new CalendarFragment());
            }
            if (itemId == FR_FRAGMENT) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentLayout, fragment);
        fragmentTransaction.commit();
    }
}