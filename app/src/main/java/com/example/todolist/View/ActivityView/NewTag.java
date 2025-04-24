package com.example.todolist.View.ActivityView;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolist.Model.Tag;
import com.example.todolist.R;
import com.example.todolist.Utils.CustomToast;
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
            String titleTag = binding.titleTag.getText().toString();

            if (titleTag.isEmpty()) {

                CustomToast.showCustomToastPlus(this, "Ủa alo? Tag chưa có tiêu đề kìa đó nghen!", Gravity.BOTTOM, R.drawable.sad);
                return;
            }

            String capitalizedTag = titleTag.substring(0, 1).toUpperCase() + titleTag.substring(1);
            tagViewModel.insert(new Tag(capitalizedTag));
            CustomToast.showCustomToast(this, "Create Tag Complete");


            finish();
        });

    }
}