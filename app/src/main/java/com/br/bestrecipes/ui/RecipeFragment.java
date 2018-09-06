package com.br.bestrecipes.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.bestrecipes.R;
import com.br.bestrecipes.bean.Recipe;
import com.br.bestrecipes.lifecycle.RecipeViewModel;
import com.br.bestrecipes.lifecycle.RecipeViewModelFactory;
import com.br.bestrecipes.service.RecipeConsumer;
import com.br.bestrecipes.service.RetrofitService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment {
    public static final String TAG = RecipeFragment.class.getSimpleName().toUpperCase();

    @BindView(R.id.recipe_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message)
    TextView tvErrorMessage;
    @BindView(R.id.progress_loading)
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecipeConsumer recipeConsumer = RetrofitService.getRetrofitService().create(RecipeConsumer.class);
        RecipeViewModelFactory factory = new RecipeViewModelFactory(recipeConsumer.getRecipes(), getActivity().getApplication());
        RecipeViewModel recipeViewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        if (isOnline()) {
            recipeViewModel.getMutableLiveDataRecipe().observe(this, new Observer<List<Recipe>>() {
                @Override
                public void onChanged(@Nullable List<Recipe> recipes) {
                    configLayout(recipes);
                }
            });
        } else {
            buildLayoutError();
        }
        initRecyclerView();
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void buildLayoutError() {
        tvErrorMessage.setText(R.string.msg_no_network);
        mRecyclerView.setAdapter(null);
        mRecyclerView.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(), getString(R.string.msg_conecte_se), Toast.LENGTH_SHORT).show();
    }

    private void initRecyclerView() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
    }

    private void configLayout(@Nullable List<Recipe> recipes) {
        if (recipes != null) {
            RecipeAdapter recipeAdapter = new RecipeAdapter(recipes, (RecipeAdapter.OnRecipeItemClick) getActivity());
            mRecyclerView.setAdapter(recipeAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);
            tvErrorMessage.setVisibility(View.GONE);
        } else {
            mRecyclerView.setAdapter(null);
            mRecyclerView.setVisibility(View.INVISIBLE);
            tvErrorMessage.setVisibility(View.VISIBLE);
            tvErrorMessage.setText(getString(R.string.msg_falha_recuperar_receitas));
        }
        progressBar.setVisibility(View.GONE);
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
