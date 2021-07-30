package com.innova.timetable.paging;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.innova.timetable.ui.MainActivityViewModel;

public class MainActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;

    public MainActivityViewModelFactory(Application application) {
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(mApplication);
    }
}
