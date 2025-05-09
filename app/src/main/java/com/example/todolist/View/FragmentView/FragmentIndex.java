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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.todolist.Model.Enum.TaskStatus;
import com.example.todolist.Model.Enum.TimeFilter;
import com.example.todolist.Model.Task;
import com.example.todolist.R;
import com.example.todolist.Utils.CoverString;
import com.example.todolist.Utils.CustomToast;
import com.example.todolist.Utils.DateUtils;
import com.example.todolist.Utils.ItemTouchHelperListener;
import com.example.todolist.Utils.RecyclerViewItemTouchHelper;
import com.example.todolist.Utils.TimeUtils;
import com.example.todolist.View.ActivityView.EditTask;
import com.example.todolist.View.rcv.FilterAdapter;
import com.example.todolist.View.rcv.TaskAdapter;
import com.example.todolist.ViewModel.TaskViewModel;
import com.example.todolist.Widget.WidgetBroadcastHelper;
import com.example.todolist.databinding.DialogDeltailTaskBinding;
import com.example.todolist.databinding.FragmentIndexBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class FragmentIndex extends Fragment implements ItemTouchHelperListener {

    private FragmentIndexBinding binding;
    private Context context;
    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIndexBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);


        taskAdapter = new TaskAdapter();
        context = getContext();


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
        if (task.getTitle() == null) {
            binding.txtTitle.setText(context.getString(R.string.newTask));
        } else {
            binding.txtTitle.setText(task.getTitle());
        }

        binding.txtTime.setText(
                task.getDueTime() != null ? TimeUtils.toLabel(task.getDueTime()) : "Any time"
        );


        if (task.getStatus() == TaskStatus.OVERDUE) {
            String strNote = "Noted on " + DateUtils.getDayLabel(task.getDueDate());
            binding.txtDes.setText(strNote);
            binding.txtStatusTask.setVisibility(View.GONE);
        } else {

            binding.txtDes.setText(task.getDescription());
            binding.iconTask.setImageResource(task.getIdIcon());

            CoverString coverString = new CoverString();
            String toStringStatus = coverString.RepeatToString(task.getRepeatFrequency(), task.getDueDate()) + " , " + coverString.Reminder(task.getReminderSetting(), task.getDueTime());
            binding.txtStatusTask.setText(coverString.Reminder(task.getReminderSetting(), task.getDueTime()));
        }

        boolean isCompleted = task.getStatus() == TaskStatus.COMPLETED;

        if (isCompleted) {
            binding.iconComplete.setVisibility(View.VISIBLE);
            binding.iconNotComplete.setVisibility(View.GONE);
        } else {
            binding.iconComplete.setVisibility(View.GONE);
            binding.iconNotComplete.setVisibility(View.VISIBLE);
        }

        binding.iconComplete.setOnClickListener(view -> {
            task.setStatus(TaskStatus.PENDING);
            taskViewModel.update(task);
            binding.iconComplete.setVisibility(View.GONE);
            binding.iconNotComplete.setVisibility(View.VISIBLE);
        });

        binding.iconNotComplete.setOnClickListener(view -> {
            if (!task.getDueDate().isAfter(LocalDate.now())) {
                task.setStatus(TaskStatus.COMPLETED);
                taskViewModel.update(task);
                binding.iconComplete.setVisibility(View.VISIBLE);
                binding.iconNotComplete.setVisibility(View.GONE);
            } else {
                CustomToast.showCustomToastPlus(context, "Let's focus today", Gravity.BOTTOM, R.drawable.hi);
            }
        });


        binding.btnEdit.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), EditTask.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("task", task);
            intent.putExtras(bundle);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void initFilter() {
        List<String> filterList = Arrays.asList(
                TimeFilter.TODAY.toString(),
                TimeFilter.ALL.toString(),
                TimeFilter.WEEK.toString(),
                TimeFilter.MONTH.toString()
        );

        FilterAdapter adapter = new FilterAdapter();
        adapter.setData(getContext(), filterList, (filter, selectedPosition) -> {
            taskViewModel.setFilter(filter);
        });



        //khởi động rcv của task
        binding.rcvTask.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvTask.setAdapter(taskAdapter);

        //main
        taskViewModel.getFilteredTaskIndex().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.setAdapter(getContext(), tasks, taskViewModel, this::openDetailDialog);
            String guess = "Bạn có " + tasks.size() + " nhiệm vụ";
            binding.txtGuess.setText(guess);

            taskViewModel.refreshOverdueTasks();

            WidgetBroadcastHelper.notifyWidgetDataChanged(requireContext());

            if (tasks.isEmpty()) {
                binding.LayoutNoTask.setVisibility(View.VISIBLE);
                binding.rcvTask.setVisibility(View.GONE);
            } else {
                binding.LayoutNoTask.setVisibility(View.GONE);
                binding.rcvTask.setVisibility(View.VISIBLE);
            }
        });


        //Khởi động rcv của filter
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcvFilter.setLayoutManager(layoutManager);
        binding.rcvFilter.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rcvTask);


    }

    private void initSearch() {
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
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

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder) {


        if (viewHolder instanceof TaskAdapter.TaskHolder) {
            int position = viewHolder.getAdapterPosition();
            Task task = taskAdapter.getTaskAt(position);

            new AlertDialog.Builder(requireContext())
                    .setTitle("❗ Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá nhiệm vụ này?")
                    .setIcon(R.drawable.logo)
                    .setPositiveButton("🗑️ Có", (dialog, which) -> {
                        taskViewModel.delete(task);
                        WidgetBroadcastHelper.notifyWidgetDataChanged(requireContext());
                        Snackbar snackbar = Snackbar.make(binding.getRoot(), "🗑️ Đã xoá nhiệm vụ", Snackbar.LENGTH_LONG)
                                .setAction("Hoàn tác", v -> taskViewModel.insert(task));
                        snackbar.show();


                    })
                    .setNegativeButton("❌ Huỷ", (dialog, which) -> {
                        taskAdapter.notifyItemChanged(position);
                        dialog.dismiss();
                    })
                    .setCancelable(false)
                    .show();

        }
    }
}