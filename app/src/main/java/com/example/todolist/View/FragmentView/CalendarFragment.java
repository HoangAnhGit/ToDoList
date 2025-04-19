package com.example.todolist.View.FragmentView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todolist.Model.Enum.TaskStatus;
import com.example.todolist.Model.Task;
import com.example.todolist.R;
import com.example.todolist.Utils.CoverString;
import com.example.todolist.View.rcv.FilterAdapter;
import com.example.todolist.View.rcv.TaskAdapter;
import com.example.todolist.ViewModel.TagViewModel;
import com.example.todolist.ViewModel.TaskViewModel;
import com.example.todolist.databinding.DialogDeltailTaskBinding;
import com.example.todolist.databinding.FragmentCalendarBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class CalendarFragment extends Fragment {


    private FragmentCalendarBinding binding;
    private TaskViewModel taskViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater,container,false);
        View mView = binding.getRoot();


        initListTask();
        initFilter();


        return mView;
    }
    private void initListTask(){

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        TaskAdapter taskAdapter = new TaskAdapter();

        binding.rcvTask.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvTask.setAdapter(taskAdapter);

        //taskViewModel.deleteAll();

        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.setAdapter(getContext(),tasks,taskViewModel, this::openDetailDialog);
        });
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
        TagViewModel tagViewModel =new  ViewModelProvider(this).get(TagViewModel.class);
        List<String> filterList = new ArrayList<>();
        filterList.add("All");
        filterList.addAll(tagViewModel.getAllTitleTagsList());

        FilterAdapter adapter = new FilterAdapter();
        adapter.setData(getContext(),filterList,(filter, selectedPosition) -> {

        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.filterTag.setLayoutManager(layoutManager);
        binding.filterTag.setAdapter(adapter);
    }
}