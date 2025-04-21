package com.example.todolist.View.ActivityView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Model.Enum.RepeatFrequency;
import com.example.todolist.Model.Tag;
import com.example.todolist.Model.Task;
import com.example.todolist.R;
import com.example.todolist.Utils.CustomToast;
import com.example.todolist.Utils.DateUtils;
import com.example.todolist.Utils.TimeUtils;
import com.example.todolist.View.rcv.ColorAdapter;
import com.example.todolist.View.rcv.ImageAdapter;
import com.example.todolist.ViewModel.TagViewModel;
import com.example.todolist.ViewModel.TaskViewModel;
import com.example.todolist.databinding.ActivityEditTaskBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EditTask extends AppCompatActivity {

    private ActivityEditTaskBinding binding;
    private Task taskEdit;
    private Tag tagSelected;
    private TaskViewModel taskViewModel;
    private TagViewModel tagViewModel;

    private final ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    int KEY_RETURN = 99;
                    int KEY_TAG_RETURN = 98;
                    if (o.getResultCode() == KEY_RETURN) {

                        assert o.getData() != null;
                        String valueReturn = o.getData().getStringExtra("VALUE_DATE");
                        binding.selectedDateText.setText(valueReturn);
                    } else if (o.getResultCode() == KEY_TAG_RETURN && o.getData() != null) {

                        Bundle bundle = o.getData().getExtras();
                        if (bundle != null && bundle.containsKey("tag")) {
                            tagSelected = (Tag) bundle.getSerializable("tag");
                            if (tagSelected != null) {
                                binding.selectedTagText.setText(tagSelected.getTitle());
                                taskEdit.setIdTag(tagSelected.getUid());
                            } else {
                                Log.e("AddTask", "Tag is null");
                            }
                        } else {
                            Log.e("AddTask", "No 'tag' key in bundle");
                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditTaskBinding.inflate(getLayoutInflater());
        View mView = binding.getRoot();
        setContentView(mView);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        tagViewModel = new ViewModelProvider(this).get(TagViewModel.class);

        Bundle tagSelect = getIntent().getExtras();
        if (tagSelect != null) {
            Serializable task = tagSelect.getSerializable("task");
            taskEdit = (Task) task;
        }

        if (taskEdit == null) {
            return;
        }
        initData(taskEdit);
        initColor();
        setupUI();
    }

    private void initData(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        binding.pickIcon.setImageResource(task.getIdIcon());
        binding.titleEdittext.setText(task.getTitle());
        binding.main.setBackgroundColor(Integer.parseInt(task.getColorCode()));
        binding.descriptionEdittext.setText(task.getDescription());
        binding.selectedDateText.setText(DateUtils.getDayLabel(task.getDueDate()));
        binding.selectedRepeatText.setText(task.getRepeatFrequency().toString());
        binding.selectedTimeText.setText(task.getDueTime() != null ? task.getDueTime().format(formatter) : "Any time");
        binding.selectedReminderText.setText(task.getReminderSetting().toString());
        tagSelected = tagViewModel.getTagByID(task.getIdTag());
        binding.selectedTagText.setText(tagSelected.getTitle());
    }

    private void setupUI() {
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
        binding.btnSave.setOnClickListener(v -> {
            String title = Objects.requireNonNull(binding.titleEdittext.getText()).toString().trim();
            String des = Objects.requireNonNull(binding.descriptionEdittext.getText()).toString().trim();
            if (taskEdit.getDueTime() == null) {
                taskEdit.setRepeatFrequency(RepeatFrequency.OFF);
            }
            if (!title.isEmpty()) {
                taskEdit.setTitle(title);
            }

            if (!des.isEmpty()) {
                taskEdit.setDescription(des);
            }
            taskViewModel.update(taskEdit);
            CustomToast.showCustomToast(this, "Update Task Complete");

            finish();
        });
        binding.pickDate.setOnClickListener(v -> setupPickDateButton());
        binding.layoutTime.setOnClickListener(v -> pickTime());

        // chưa set được giá trị default
//        binding.layoutRepeat.setOnClickListener(v -> showRepeatDialog());
//        binding.layoutReminder.setOnClickListener(v -> showReminderDialog());
        binding.pickIcon.setOnClickListener(v -> pickIcon());
        binding.layoutTag.setOnClickListener(v -> {
            Intent tagIntent = new Intent(this, TagActivity.class);
            Bundle bundle = new Bundle();

            tagIntent.putExtra("Value_Color", taskEdit.getColorCode());
            bundle.putSerializable("TAG_SELECT", tagSelected);
            tagIntent.putExtras(bundle);
            intentActivityResultLauncher.launch(tagIntent);
        });
    }

    private void pickTime() {
        binding.layoutTime.setOnClickListener(view -> {
            int initialHour = (taskEdit.getDueTime() != null) ? taskEdit.getDueTime().getHour() : 10;
            int initialMinute = (taskEdit.getDueTime() != null) ? taskEdit.getDueTime().getMinute() : 30;

            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(initialHour)
                    .setMinute(initialMinute)
                    .setTitleText("Chọn thời gian")
                    .build();

            picker.show(getSupportFragmentManager(), "TIME_PICKER");

            picker.addOnPositiveButtonClickListener(v -> {
                int hour = picker.getHour();
                int minute = picker.getMinute();
                LocalTime selectedTime = LocalTime.of(hour, minute);

                taskEdit.setDueTime(selectedTime);
                binding.selectedTimeText.setText(TimeUtils.toLabel(selectedTime));
            });
        });
    }


    private void setupPickDateButton() {
        binding.pickDate.setOnClickListener(view -> {
            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now());

            long initialSelection;
            if (taskEdit.getDueDate() != null) {
                initialSelection = taskEdit.getDueDate()
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant()
                        .toEpochMilli();
            } else {
                initialSelection = MaterialDatePicker.todayInUtcMilliseconds();
            }

            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Chọn ngày")
                    .setSelection(initialSelection)
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build();

            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                LocalDate picked = Instant.ofEpochMilli(selection)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate();

                taskEdit.setDueDate(picked);
                binding.selectedDateText.setText(DateUtils.getDayLabel(taskEdit.getDueDate()));
            });
        });
    }

    private void initColor() {
        RecyclerView recyclerView = findViewById(R.id.rvColorOptions);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<Integer> colors = Arrays.asList(
                R.color.item_blue,
                R.color.item_pink,
                R.color.item_orange,
                R.color.item_green,
                R.color.item_yellow,
                R.color.item_light_green,
                R.color.item_purple
        );

        ColorAdapter colorAdapter = new ColorAdapter(this, colors, colorResourceId -> {

            int color = ContextCompat.getColor(this, colorResourceId);
            binding.main.setBackgroundColor(color);
            taskEdit.setColorCode(String.valueOf(color));
        });
        recyclerView.setAdapter(colorAdapter);
    }

    private void pickIcon() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.diaglog_img, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.rcv_img);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));


        List<Integer> images = Arrays.asList(
                R.drawable.bird,
                R.drawable.chines,
                R.drawable.eat,
                R.drawable.fly,
                R.drawable.graduation,
                R.drawable.hi,
                R.drawable.love,
                R.drawable.study,
                R.drawable.write
        );
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        ImageAdapter adapter = new ImageAdapter(images, imageResId -> {
            taskEdit.setIdIcon(imageResId);
            binding.pickIcon.setImageResource(imageResId);
            dialog.dismiss();
        });

        recyclerView.setAdapter(adapter);

        dialog.show();
    }
}