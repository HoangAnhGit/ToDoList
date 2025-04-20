package com.example.todolist.View.FragmentView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.todolist.Model.Enum.TaskStatus;
import com.example.todolist.Model.Enum.TimeFilter;
import com.example.todolist.Model.Task;
import com.example.todolist.Utils.CoverString;
import com.example.todolist.View.rcv.FilterAdapter;
import com.example.todolist.View.rcv.TaskAdapter;
import com.example.todolist.ViewModel.TaskViewModel;
import com.example.todolist.databinding.DialogDeltailTaskBinding;
import com.example.todolist.databinding.FragmentIndexBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class FragmentIndex extends Fragment {

    private FragmentIndexBinding binding;
    private TaskViewModel taskViewModel;
    private   TaskAdapter taskAdapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIndexBinding.inflate(inflater,container,false);
        View mView = binding.getRoot();
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskAdapter = new TaskAdapter();


        initFilter();
        initSearch();

        return mView;
    }


    private void openDetailDialog(Task task) {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());

        DialogDeltailTaskBinding binding = DialogDeltailTaskBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        binding.layoutDetailItem.setBackgroundColor(Integer.parseInt(task.getColorCode()));
        binding.txtTitle.setText(task.getTitle());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        binding.txtTime.setText(
                task.getDueTime() != null ? task.getDueTime().format(formatter) : "Any time"
        );
        binding.txtDes.setText(task.getDescription());
        binding.iconTask.setImageResource(task.getIdIcon());

        CoverString coverString = new CoverString();
        String toStringStatus = coverString.RepeatToString(task.getRepeatFrequency(),task.getDueDate())+" , "+coverString.Reminder(task.getReminderSetting(),task.getDueTime());
        binding.txtStatusTask.setText(toStringStatus);


        boolean isCompleted = task.getStatus().equals(TaskStatus.COMPLETED);
        binding.iconComplete.setVisibility(isCompleted ? View.VISIBLE : View.GONE);
        binding.iconNotComplete.setVisibility(isCompleted ? View.GONE : View.VISIBLE);


        binding.btnEdit.setOnClickListener(v -> {

            dialog.dismiss();
        });

        dialog.show();
    }

    private void initFilter(){
            List<String> filterList =Arrays.asList(
                    TimeFilter.TODAY.toString(),
                    TimeFilter.ALL.toString(),
                    TimeFilter.WEEK.toString(),
                    TimeFilter.MONTH.toString()
            );

        FilterAdapter adapter = new FilterAdapter();
        adapter.setData(getContext(),filterList,(filter, selectedPosition) -> {
            taskViewModel.setFilter(filter);
        });

        //taskViewModel.deleteAll();

        //khởi động rcv của task
        binding.rcvTask.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvTask.setAdapter(taskAdapter);
        taskViewModel.getFilteredTaskIndex().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.setAdapter(getContext(), tasks, taskViewModel, this::openDetailDialog);
        });

        //Khởi động rcv của filter
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcvFilter.setLayoutManager(layoutManager);
        binding.rcvFilter.setAdapter(adapter);

    }

    private void initSearch(){
            binding.searchBar.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    taskAdapter.getFilter().filter(s.toString());
                }

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(Editable s) {}
            });

        binding.iconSearch.setOnClickListener(v -> {
            binding.searchBar.requestFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.searchBar, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        binding.searchBar.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.iconCancel.setVisibility(View.VISIBLE);
            } else {
                binding.iconCancel.setVisibility(View.GONE);
            }
        });

        binding.iconCancel.setOnClickListener(v -> {
            binding.searchBar.setText("");
            binding.searchBar.clearFocus();

            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(binding.searchBar.getWindowToken(), 0);
            }
        });
    }

}