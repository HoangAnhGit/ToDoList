package com.example.todolist.View.ActivityView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.todolist.Model.Enum.ReminderSetting;
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
import com.example.todolist.databinding.ActivityAddTaskBinding;
import com.example.todolist.databinding.DialogReminderBinding;
import com.example.todolist.databinding.DialogRepeatBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddTask extends AppCompatActivity {

    private ActivityAddTaskBinding binding;
    private Task newTask;
    private Tag tagSelected =null;
    private TaskViewModel taskViewModel;
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
                    }else if (o.getResultCode() == KEY_TAG_RETURN && o.getData() != null) {

                        Bundle bundle = o.getData().getExtras();
                        if (bundle != null && bundle.containsKey("tag")) {
                             tagSelected = (Tag) bundle.getSerializable("tag");
                            if (tagSelected != null) {
                                binding.selectedTagText.setText(tagSelected.getTitle());
                                newTask.setIdTag(tagSelected.getUid());
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
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        View mview = binding.getRoot();
        setContentView(mview);
        newTask = new Task(mview.getContext());


        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        setupUI();
        initData();
        initColor();
    }

    private void setupUI() {
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
        binding.btnCreate.setOnClickListener(v -> {
            String title = Objects.requireNonNull(binding.titleEdittext.getText()).toString().trim();
            String des = Objects.requireNonNull(binding.descriptionEdittext.getText()).toString().trim();
            if(newTask.getDueTime()==null){
                newTask.setRepeatFrequency(RepeatFrequency.OFF);
            }
            if (!title.isEmpty()) {
                newTask.setTitle(title);
            }

            if (!des.isEmpty()) {
                newTask.setDescription(des);
            }
            taskViewModel.insert(newTask);
            CustomToast.showCustomToast(this,"Create Task Complete");

            finish();
        });
        binding.pickDate.setOnClickListener(v -> setupPickDateButton());
        binding.layoutTime.setOnClickListener(v -> pickTime());
        binding.layoutRepeat.setOnClickListener(v -> showRepeatDialog());
        binding.layoutReminder.setOnClickListener(v -> showReminderDialog());
        binding.pickIcon.setOnClickListener(v -> pickIcon());
        binding.layoutTag.setOnClickListener(v -> {
            Intent tagIntent = new Intent(this, TagActivity.class);
            Bundle bundle = new Bundle();
            tagIntent.putExtra("Value_Color",newTask.getColorCode());

            Tag tagSelecting = new TagViewModel(getApplication()).getTagByID(newTask.getIdTag());
            bundle.putSerializable("TAG_SELECT",tagSelecting);
            tagIntent.putExtras(bundle);
            intentActivityResultLauncher.launch(tagIntent);
        });
    }

    private void initData(){

    }
    private void pickTime() {
        binding.layoutTime.setOnClickListener(view -> {
            int initialHour = (newTask.getDueTime() != null) ? newTask.getDueTime().getHour() : 10;
            int initialMinute = (newTask.getDueTime() != null) ? newTask.getDueTime().getMinute() : 30;

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

                newTask.setDueTime(selectedTime);
                binding.selectedTimeText.setText(TimeUtils.toLabel(selectedTime));
            });
        });
    }


    private void setupPickDateButton() {
        binding.pickDate.setOnClickListener(view -> {
            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now());

            long initialSelection;
            if (newTask.getDueDate() != null) {
                initialSelection = newTask.getDueDate()
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant()
                        .toEpochMilli();
            } else {
                initialSelection = MaterialDatePicker.todayInUtcMilliseconds();
            }

            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Calendar")
                    .setSelection(initialSelection)
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build();

            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                LocalDate picked = Instant.ofEpochMilli(selection)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate();

                newTask.setDueDate(picked);
                binding.selectedDateText.setText(DateUtils.getDayLabel(newTask.getDueDate()));
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
            newTask.setColorCode(String.valueOf(color));
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
            newTask.setIdIcon(imageResId);
            binding.pickIcon.setImageResource(imageResId);
            dialog.dismiss();
        });

        recyclerView.setAdapter(adapter);

        dialog.show();
    }

    private void showRepeatDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        DialogRepeatBinding bindingRepeat = DialogRepeatBinding.inflate(getLayoutInflater());
        dialog.setContentView(bindingRepeat.getRoot());

        LocalDate taskDate = newTask.getDueDate();
        if (taskDate == null) {
            taskDate = LocalDate.now();
        }

        String dayOfWeek = taskDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int dayOfMonth = taskDate.getDayOfMonth();
        String dayWithSuffix = dayOfMonth + getDaySuffix(dayOfMonth);


        bindingRepeat.weekly.setText("Weekly (" + dayOfWeek + ")");
        bindingRepeat.monthly.setText("Monthly (On " + dayWithSuffix + ")");


        bindingRepeat.repeatOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.noRepeat) {
                binding.selectedRepeatText.setText(bindingRepeat.noRepeat.getText().toString());
                bindingRepeat.noRepeat.setChecked(true);
                newTask.setRepeatFrequency(RepeatFrequency.OFF);
            } else if (checkedId == R.id.daily) {
                binding.selectedRepeatText.setText(bindingRepeat.daily.getText().toString());
                bindingRepeat.daily.setChecked(true);
                newTask.setRepeatFrequency(RepeatFrequency.DAILY);
            } else if (checkedId == R.id.weekly) {
                binding.selectedRepeatText.setText(bindingRepeat.weekly.getText().toString());
                bindingRepeat.weekly.setChecked(true);
                newTask.setRepeatFrequency(RepeatFrequency.WEEKLY);
            } else if (checkedId == R.id.monthly) {
                binding.selectedRepeatText.setText(bindingRepeat.monthly.getText().toString());
                bindingRepeat.monthly.setChecked(true);
                newTask.setRepeatFrequency(RepeatFrequency.MONTHLY);
            }
            new Handler().postDelayed(dialog::dismiss, 400);
        });

        dialog.show();
    }

    private String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) return "th";
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    private void showReminderDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        DialogReminderBinding bindingReminder = DialogReminderBinding.inflate(getLayoutInflater());
        dialog.setContentView(bindingReminder.getRoot());

        bindingReminder.reminderOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.noReminder) {
                newTask.setReminderSetting(ReminderSetting.NO_REMINDER);
            } else if (checkedId == R.id.onEvent) {
                newTask.setReminderSetting(ReminderSetting.AT_TIME_OF_DUE);
            } else if (checkedId == R.id.fifteenMinutesBefore) {
                newTask.setReminderSetting(ReminderSetting.FIFTEEN_MINUTES_BEFORE);
            } else if (checkedId == R.id.oneHourBefore) {
                newTask.setReminderSetting(ReminderSetting.ONE_HOUR_BEFORE);
            }

            updateReminderTextView();

            new Handler().postDelayed(dialog::dismiss, 400);
        });

        dialog.show();
    }

    private void updateReminderTextView() {
        String reminderText = "No reminder";

        if (newTask.getReminderSetting() == ReminderSetting.FIFTEEN_MINUTES_BEFORE) {
            reminderText = "Before event 15 minutes";
        } else if (newTask.getReminderSetting() == ReminderSetting.ONE_HOUR_BEFORE) {
            reminderText = "Before event 1 hour";
        }else if (newTask.getReminderSetting() == ReminderSetting.AT_TIME_OF_DUE) {
            reminderText = "At time of event";
        }

        binding.selectedReminderText.setText(reminderText);
    }
}