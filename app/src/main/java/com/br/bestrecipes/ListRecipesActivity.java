package com.br.bestrecipes;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.br.bestrecipes.bean.Recipe;
import com.br.bestrecipes.ui.RecipeAdapter;
import com.br.bestrecipes.ui.RecipeDetailFragment;
import com.br.bestrecipes.ui.RecipeFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class ListRecipesActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeItemClick {
    public static final String BUNDLE_RECIPE = "recipe";
    public static final String BUNDLE_INGREDIENTS = "ingredients";
    public static final String BUNDLE_STEPS = "steps";

    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recipes);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            RecipeFragment fragment = new RecipeFragment();
            ft.add(R.id.container_fragment, fragment).addToBackStack(RecipeFragment.TAG);
            ft.commit();
        }

    }

    @Override
    public void onItemClick(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_RECIPE, recipe);
        bundle.putParcelableArrayList(BUNDLE_INGREDIENTS, (ArrayList<? extends Parcelable>) recipe.getIngredients());
        bundle.putParcelableArrayList(BUNDLE_STEPS, (ArrayList<? extends Parcelable>) recipe.getSteps());

        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);

        ft = fm.beginTransaction();
        ft.replace(R.id.container_fragment, fragment).addToBackStack(RecipeDetailFragment.TAG);
        ft.commit();
    }
}
