package com.example.todolist.View.FragmentView;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.ViewModel.TagViewModel;
import com.example.todolist.ViewModel.TaskViewModel;
import com.example.todolist.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {


    private FragmentProfileBinding binding;
    private TaskViewModel taskViewModel;
    private TagViewModel tagViewModel;
    private Context context;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        tagViewModel =  new ViewModelProvider(this).get(TagViewModel.class);
        View mView = binding.getRoot();

        context = getContext();
       initData();
       setDarkMode(false);

        return mView;
    }

    private void initData() {
        taskViewModel.getTotalTaskCount().observe(getViewLifecycleOwner(), total -> {
            binding.tvTaskTotal.setText(String.valueOf(total));
        });

        taskViewModel.getCompletedTaskCount().observe(getViewLifecycleOwner(), completed -> {
            binding.tvTaskComplete.setText(String.valueOf(completed));
        });

        binding.reset.setOnClickListener(view -> showConfirmResetAllTasks());
    }

    private void showConfirmResetAllTasks() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Reset");
        builder.setIcon(R.drawable.logo);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_reset, null);
        EditText input = dialogView.findViewById(R.id.edt_confirm_reset);

        builder.setView(dialogView);
        builder.setPositiveButton("Confirm", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            Button confirmButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            confirmButton.setOnClickListener(v -> {
                String text = input.getText().toString().trim();
                if (text.equalsIgnoreCase("RESET")) {
                    taskViewModel.deleteAll();
                    tagViewModel.deleteAll();
                    Toast.makeText(context, "All tasks have been deleted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    input.setError("Please type RESET to confirm");
                }
            });
        });

        dialog.show();
    }
    public void setDarkMode(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}