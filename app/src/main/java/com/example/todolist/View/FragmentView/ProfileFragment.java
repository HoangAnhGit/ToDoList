package com.example.todolist.View.FragmentView;

import android.content.Context;
import android.content.res.Configuration;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.Utils.CustomToast;
import com.example.todolist.ViewModel.TagViewModel;
import com.example.todolist.ViewModel.TaskViewModel;
import com.example.todolist.databinding.FragmentProfileBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;


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
        tagViewModel = new ViewModelProvider(this).get(TagViewModel.class);
        View mView = binding.getRoot();

        context = getContext();
        initData(mView);
        setDarkMode(false);

        return mView;
    }

    private void initData( View mView) {
        taskViewModel.getTotalTaskCount().observe(getViewLifecycleOwner(), total -> {
            binding.tvTaskTotal.setText(String.valueOf(total));
        });

        taskViewModel.getCompletedTaskCount().observe(getViewLifecycleOwner(), completed -> {
            binding.tvTaskComplete.setText(String.valueOf(completed));
        });

        binding.reset.setOnClickListener(view -> showConfirmResetAllTasks());

        binding.ui.setOnClickListener(view->{showBottomSheetTheme(mView);
        });

        binding.help.setOnClickListener(view->{
            showIntroBottomSheet(mView);
        });
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
                    //tagViewModel.deleteAll();
                    CustomToast.showCustomToast(getContext(),"All tasks have been deleted");
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

    private void showBottomSheetTheme(View mView) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mView.getContext());
        View view = LayoutInflater.from(mView.getContext()).inflate(R.layout.bottom_sheet_theme, null);
        bottomSheetDialog.setContentView(view);

        RadioGroup radioGroup = view.findViewById(R.id.radioGroupTheme);
        RadioButton radioLight = view.findViewById(R.id.radioLight);
        RadioButton radioDark = view.findViewById(R.id.radioDark);

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            radioLight.setChecked(true);
        } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            radioDark.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioLight) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (checkedId == R.id.radioDark) {
              setDarkMode(true);
            }
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void showIntroBottomSheet(View mView) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mView.getContext());
        View view = LayoutInflater.from(mView.getContext()).inflate(R.layout.help, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }
}