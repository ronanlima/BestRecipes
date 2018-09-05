package com.br.bestrecipes.lifecycle;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.br.bestrecipes.bean.Recipe;

import java.util.List;

import retrofit2.Call;

public class RecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Call<List<Recipe>> mRecipes;
    private Application application;

    public RecipeViewModelFactory(Call<List<Recipe>> mRecipes, Application application) {
        this.mRecipes = mRecipes;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeViewModel(application, mRecipes);
    }
}
