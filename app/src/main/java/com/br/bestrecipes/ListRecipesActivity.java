package com.br.bestrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.br.bestrecipes.bean.Recipe;
import com.br.bestrecipes.ui.RecipeAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class ListRecipesActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeItemClick {
    public static final String RECIPE_KEY = "recipe";
    public static final String INGREDIENTS_KEY = "ingredients";
    public static final String STEPS_KEY = "steps";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recipes);

        ButterKnife.bind(this);
    }

    @Override
    public void onItemClick(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_KEY, recipe);
        bundle.putParcelableArrayList(INGREDIENTS_KEY, (ArrayList<? extends Parcelable>) recipe.getIngredients());
        bundle.putParcelableArrayList(STEPS_KEY, (ArrayList<? extends Parcelable>) recipe.getSteps());
        Intent intent = new Intent(this, null);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
