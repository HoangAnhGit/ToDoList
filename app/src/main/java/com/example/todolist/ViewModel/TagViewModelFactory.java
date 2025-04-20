package com.example.todolist.ViewModel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.annotations.NonNull;

public class TagViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public TagViewModelFactory(Application application) {
        this.application = application;
    }

    @androidx.annotation.NonNull
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TagViewModel.class)) {
            return (T) new TagViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
