package com.example.todolist.ViewModel;

import android.os.CountDownTimer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FocusViewModel extends ViewModel {
    private final MutableLiveData<Long> timeLeft = new MutableLiveData<>(0L);
    private final MutableLiveData<Boolean> isRunning = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> selectedIndex = new MutableLiveData<>(2);

    private CountDownTimer timer;

    private final MutableLiveData<Boolean> _timerFinished = new MutableLiveData<>(false);
    public LiveData<Boolean> getTimerFinished() { return _timerFinished; }
    private long endTime;

    public LiveData<Long> getTimeLeft() { return timeLeft; }
    public LiveData<Boolean> getIsRunning() { return isRunning; }
    public LiveData<Integer> getSelectedIndex() { return selectedIndex; }

    public void setSelectedIndex(int index) {
        selectedIndex.setValue(index);
    }

    public void resetTimerFinished() {
        _timerFinished.setValue(false);
    }

    public void startTimer(long millis) {
        if (timer != null) timer.cancel();
        endTime = System.currentTimeMillis() + millis;
        timer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft.postValue(millisUntilFinished);
            }
            @Override
            public void onFinish() {
                timeLeft.postValue(0L);
                isRunning.postValue(false);
                _timerFinished.postValue(true);
            }
        }.start();
        isRunning.setValue(true);
    }

    public void pauseTimer() {
        if (timer != null) {
            timer.cancel();
            long left = endTime - System.currentTimeMillis();
            timeLeft.setValue(left > 0 ? left : 0);
            isRunning.setValue(false);
        }
    }

    public void resumeTimer() {
        Long left = timeLeft.getValue();
        if (left != null && left > 0) {
            startTimer(left);
        }
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timeLeft.setValue(0L);
        isRunning.setValue(false);
    }

    @Override
    protected void onCleared() {
        if (timer != null) timer.cancel();
        super.onCleared();
    }
}
