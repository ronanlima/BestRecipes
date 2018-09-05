package com.br.bestrecipes.lifecycle;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.br.bestrecipes.bean.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeViewModel extends AndroidViewModel {
    private MutableLiveData<List<Recipe>> mutableLiveDataRecipe;

    public RecipeViewModel(@NonNull Application application, Call<List<Recipe>> recipes) {
        super(application);
        if (mutableLiveDataRecipe == null) {
            mutableLiveDataRecipe = new MutableLiveData<>();
        }
        requestRecipes(recipes);
    }

    private void requestRecipes(Call<List<Recipe>> recipes) {
        recipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mutableLiveDataRecipe.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                mutableLiveDataRecipe.setValue(null);
            }
        });
    }

    public MutableLiveData<List<Recipe>> getMutableLiveDataRecipe() {
        return mutableLiveDataRecipe;
    }
}
