package com.example.todolist.View.FragmentView;
import android.app.Notification;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;


import com.example.todolist.R;
import com.example.todolist.Utils.FocusModeService;
import com.example.todolist.Utils.MyApplicationChanel;
import com.example.todolist.ViewModel.FocusViewModel;
import com.example.todolist.databinding.FragmentFocusBinding;

import java.util.Date;

public class FocusFragment extends Fragment {

    private FragmentFocusBinding binding;
    private FocusViewModel viewModel;
    private static final String[] POMODORO_VALUES = {"5", "15", "25", "30", "45", "60"};
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFocusBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(requireActivity()).get(FocusViewModel.class);

        initPickNumber();
        initUIListeners();
        observeViewModel();

        return view;
    }


    private void initPickNumber() {
        NumberPicker picker = binding.selectedNumber;
        picker.setMinValue(0);
        picker.setMaxValue(POMODORO_VALUES.length - 1);
        picker.setDisplayedValues(POMODORO_VALUES);
        picker.setValue(viewModel.getSelectedIndex().getValue());
        picker.setOnValueChangedListener((np, oldVal, newVal) -> viewModel.setSelectedIndex(newVal));
    }

    private void initUIListeners() {
        binding.btnStart.setOnClickListener(v -> {
            int idx = viewModel.getSelectedIndex().getValue();
            long millis = Long.parseLong(POMODORO_VALUES[idx]) * 60 * 1000L;

//            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
//            startActivity(intent);

            FocusModeService.setFocusMode(true);

            viewModel.startTimer(millis);
        });
        binding.btnPause.setOnClickListener(v -> {
            FocusModeService.setFocusMode(false);
            viewModel.pauseTimer();
        });

        binding.btnContinue.setOnClickListener(v -> {
            FocusModeService.setFocusMode(true);
            viewModel.resumeTimer();
        });

        binding.btnCancel.setOnClickListener(v -> {
            FocusModeService.setFocusMode(false);
            viewModel.cancelTimer();
        });
    }

    private void observeViewModel() {
        viewModel.getTimeLeft().observe(getViewLifecycleOwner(), millis -> {
            updateCountdownText(millis);
            updateProgressBar(millis);
        });
        viewModel.getIsRunning().observe(getViewLifecycleOwner(), running -> {
            Long left = viewModel.getTimeLeft().getValue();
            if (Boolean.TRUE.equals(running)) {
                binding.selectedNumber.setVisibility(View.GONE);
                binding.tvTime.setVisibility(View.VISIBLE);
                showOnlyPauseButton();
            } else if (left != null && left > 0) {
                binding.selectedNumber.setVisibility(View.GONE);
                binding.tvTime.setVisibility(View.VISIBLE);
                showContinueCancelButtons();
            } else {
                FocusModeService.setFocusMode(false);

                binding.selectedNumber.setVisibility(View.VISIBLE);
                binding.tvTime.setVisibility(View.GONE);
                binding.processBar.setProgress(0);
                showOnlyStartButton();
            }
        });
        viewModel.getSelectedIndex().observe(getViewLifecycleOwner(), idx -> {
            if (binding.selectedNumber.getValue() != idx) {
                binding.selectedNumber.setValue(idx);
            }
        });

        viewModel.getTimerFinished().observe(getViewLifecycleOwner(), finished -> {
            if (Boolean.TRUE.equals(finished)) {
                sendFocusFinishedNotification();
                viewModel.resetTimerFinished();
            }
        });
    }

    private void sendFocusFinishedNotification() {
        Notification notification = new NotificationCompat.Builder(requireContext(), MyApplicationChanel.CHANNEL_ID)
                .setContentTitle("Focus Time hoàn thành!")
                .setContentText("Bạn đã hoàn thành phiên tập trung. Thư giãn một chút nhé!")
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(requireContext());
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManagerCompat.notify(getNotificationId(), notification);
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }

    private void updateCountdownText(long timeLeftInMillis) {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        binding.tvTime.setText(String.format("%02d : %02d", minutes, seconds));
    }

    private void updateProgressBar(long timeLeftInMillis) {
        int idx = viewModel.getSelectedIndex().getValue();
        long total = Long.parseLong(POMODORO_VALUES[idx]) * 60 * 1000L;
        binding.processBar.setProgress((int) ((total - timeLeftInMillis) * 100 / total));
    }

    private void showOnlyStartButton() {
        binding.btnStart.setVisibility(View.VISIBLE);
        binding.btnPause.setVisibility(View.GONE);
        binding.btnContinue.setVisibility(View.GONE);
        binding.btnCancel.setVisibility(View.GONE);
    }

    private void showOnlyPauseButton() {
        binding.btnStart.setVisibility(View.GONE);
        binding.btnPause.setVisibility(View.VISIBLE);
        binding.btnContinue.setVisibility(View.GONE);
        binding.btnCancel.setVisibility(View.GONE);
    }

    private void showContinueCancelButtons() {
        binding.btnStart.setVisibility(View.GONE);
        binding.btnPause.setVisibility(View.GONE);
        binding.btnContinue.setVisibility(View.VISIBLE);
        binding.btnCancel.setVisibility(View.VISIBLE);
    }

}
