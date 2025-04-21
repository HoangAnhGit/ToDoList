package com.example.todolist.View.FragmentView;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todolist.Calendar.DayAdapter;
import com.example.todolist.Calendar.MothAdapter;
import com.example.todolist.Model.Enum.TaskStatus;
import com.example.todolist.Model.Tag;
import com.example.todolist.Model.Task;
import com.example.todolist.R;
import com.example.todolist.Utils.CalendarUtils;
import com.example.todolist.Utils.CoverString;
import com.example.todolist.View.rcv.FilterAdapter;
import com.example.todolist.View.rcv.TaskAdapter;
import com.example.todolist.ViewModel.TagViewModel;
import com.example.todolist.ViewModel.TaskViewModel;
import com.example.todolist.databinding.DialogDeltailTaskBinding;
import com.example.todolist.databinding.FragmentCalendarBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


public class CalendarFragment extends Fragment {


    private FragmentCalendarBinding binding;
    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskAdapter = new TaskAdapter();

        initFilter();
        initCalendar();


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
        String toStringStatus = coverString.RepeatToString(task.getRepeatFrequency(), task.getDueDate()) + " , " + coverString.Reminder(task.getReminderSetting(), task.getDueTime());
        binding.txtStatusTask.setText(toStringStatus);


        boolean isCompleted = task.getStatus().equals(TaskStatus.COMPLETED);
        binding.iconComplete.setVisibility(isCompleted ? View.VISIBLE : View.GONE);
        binding.iconNotComplete.setVisibility(isCompleted ? View.GONE : View.VISIBLE);


        binding.btnEdit.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void initFilter() {
        TagViewModel tagViewModel = new ViewModelProvider(this).get(TagViewModel.class);

        tagViewModel.getAllTags().observe(getViewLifecycleOwner(), tags -> {
            List<String> filterList = new ArrayList<>();
            filterList.add("All");

            for (Tag tag : tags) {
                filterList.add(tag.getTitle());
            }

            //rcv filter
            FilterAdapter adapter = new FilterAdapter();
            adapter.setData(getContext(), filterList, (filter, selectedPosition) -> taskViewModel.filterTasksByTagId(selectedPosition));

            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            binding.filterTag.setLayoutManager(layoutManager);
            binding.filterTag.setAdapter(adapter);


            //rcv task
            binding.rcvTask.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rcvTask.setAdapter(taskAdapter);
            taskViewModel.getFilteredTasks().observe(getViewLifecycleOwner(), tasks -> taskAdapter.setAdapter(getContext(), tasks, taskViewModel, this::openDetailDialog));
        });
    }

    private void initCalendar() {

        binding.rcvMothChooser.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        MothAdapter mothAdapter = new MothAdapter();
        binding.rcvMothChooser.setAdapter(mothAdapter);


        AtomicReference<YearMonth> currentMonth = new AtomicReference<>(YearMonth.now());
        List<LocalDate> dayList = CalendarUtils.getDaysInMonth(currentMonth.get());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcvDayChooser.setLayoutManager(layoutManager);
        DayAdapter dayAdapter = new DayAdapter(getContext(),dayList,dateSelected -> {

        });
        binding.rcvDayChooser.setAdapter(dayAdapter);


        mothAdapter.setOnClick(newMonth -> {
            currentMonth.set(newMonth);
            List<LocalDate> newDays = CalendarUtils.getDaysInMonth(currentMonth.get());
            dayAdapter.setNewData(newDays);
        });


    }
}
