package com.alcpluralsight.aad_team20.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String sort_criteria;
    public MainViewModelFactory(String sort_criteria){
        this.sort_criteria = sort_criteria;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return super.create(modelClass);
    }
}
