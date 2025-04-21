package com.example.todolist.View.FragmentView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.todolist.Utils.ItemTouchHelperListener;
import com.example.todolist.Utils.RecyclerViewItemTouchHelper;
import com.example.todolist.View.ActivityView.EditTask;
import com.example.todolist.View.rcv.FilterAdapter;
import com.example.todolist.View.rcv.TaskAdapter;
import com.example.todolist.ViewModel.TagViewModel;
import com.example.todolist.ViewModel.TaskViewModel;
import com.example.todolist.databinding.DialogDeltailTaskBinding;
import com.example.todolist.databinding.FragmentCalendarBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;


public class CalendarFragment extends Fragment implements ItemTouchHelperListener {


    private FragmentCalendarBinding binding;
    private TaskViewModel taskViewModel;
    private  Context  context ;
    private TaskAdapter taskAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskAdapter = new TaskAdapter();

        context = getContext();

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


        if(task.getTitle()==null){
            binding.txtTitle.setText(context.getString(R.string.newTask));
        }else {
            binding.txtTitle.setText(task.getTitle());
        }

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


        binding.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditTask.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("task",task);
            intent.putExtras(bundle);
            startActivity(intent);
            dialog.dismiss();
        });



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

            ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                    new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rcvTask);
            taskViewModel.filteredTasksIdTagDate().observe(getViewLifecycleOwner(), tasks -> taskAdapter.setAdapter(getContext(), tasks, taskViewModel, this::openDetailDialog));
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
        DayAdapter dayAdapter = new DayAdapter(getContext(),dayList,dateSelected -> taskViewModel.setSelectedDate(dateSelected));
        binding.rcvDayChooser.setAdapter(dayAdapter);
        dayAdapter.scrollToSelectedDate(binding.rcvDayChooser);

        // hiện dot
        taskViewModel.getUnfinishedTaskDates().observe(getViewLifecycleOwner(), unfinishedDates -> {
            if (unfinishedDates != null) {
                Set<LocalDate> set = new HashSet<>(unfinishedDates);
                dayAdapter.setDaysWithUnfinishedTasks(set);
            }
        });


        mothAdapter.setOnClick(newMonth -> {
            currentMonth.set(newMonth);
            List<LocalDate> newDays = CalendarUtils.getDaysInMonth(currentMonth.get());
            dayAdapter.setNewData(newDays);
        });
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof TaskAdapter.TaskHolder) {
            int position = viewHolder.getAdapterPosition();
            Task task = taskAdapter.getTaskAt(position);

            new AlertDialog.Builder(requireContext())
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá nhiệm vụ này?")
                    .setPositiveButton("Có", (dialog, which) -> {

                        taskViewModel.delete(task);
                        Snackbar snackbar = Snackbar.make(binding.getRoot(), "🗑️ Đã xoá nhiệm vụ", Snackbar.LENGTH_LONG)
                                .setAction("Hoàn tác", v -> taskViewModel.insert(task));
                        snackbar.show();


                    })
                    .setNegativeButton("Huỷ", (dialog, which) -> {
                        taskAdapter.notifyItemChanged(position);
                        dialog.dismiss();
                    })
                    .setCancelable(false)
                    .show();
        }
    }
}
