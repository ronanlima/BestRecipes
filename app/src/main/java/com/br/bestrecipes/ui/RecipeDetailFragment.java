package com.br.bestrecipes.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.br.bestrecipes.ListRecipesActivity;
import com.br.bestrecipes.R;
import com.br.bestrecipes.bean.Ingredient;
import com.br.bestrecipes.bean.Recipe;
import com.br.bestrecipes.bean.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeDetailFragment extends Fragment {
    public static final String TAG = RecipeDetailFragment.class.getSimpleName().toUpperCase();

    private List<Step> steps;
    private List<Ingredient> ingredients;
    private Recipe recipe;
    private int actualStep;

    @BindView(R.id.tv_label_steps)
    TextView tvStepLabel;
    @BindView(R.id.tv_step_value)
    TextView tvStepDescription;
    @BindView(R.id.tv_ingredients_value)
    TextView tvIngredients;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_prev)
    TextView tvPrev;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFieldsFromListRecipeActivity();
        configureIngredients();
        configureActualStep();
        setRetainInstance(true);
    }

    /**
     * Configure the label of the actual step.
     */
    private void configureActualStep() {
        int position = 0;
        if (actualStep >= steps.size()) {
            position = steps.size() - 1;
        } else {
            position = actualStep;
        }
        tvStepLabel.setText(String.format("Step %s - %s", (actualStep + 1), steps.get(position).getShortDescription()));
        if (actualStep != 0) {
            tvStepDescription.setText(steps.get(position).getDescription());
        }
        if (!hasNext()) {
            tvNext.setBackgroundColor(getResources().getColor(R.color.color_disabled));
        } else {
            tvNext.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        if (hasPrevious()) {
            tvPrev.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            tvPrev.setBackgroundColor(getResources().getColor(R.color.color_disabled));
        }
    }

    @OnClick(R.id.tv_next)
    public void next() {
        if (hasNext()) {
            actualStep++;
            configureActualStep();
        }
    }

    @OnClick(R.id.tv_prev)
    public void prev() {
        if (hasPrevious()) {
            actualStep--;
            configureActualStep();
        }
    }

    /**
     * Config a textview with the ingredients of recipe.
     */
    private void configureIngredients() {
        StringBuilder builder = new StringBuilder();
        for (Ingredient i : ingredients) {
            builder.append(i.getQuantity());
            builder.append(" ").append(i.getIngredient()).append("\n");
            builder.append(i.getMeasure()).append("\n");
        }
        tvIngredients.setText(builder.toString());
    }

    /**
     * Initialize objects with value received from previous activity.
     */
    private void initFieldsFromListRecipeActivity() {
        if (steps == null) {
            steps = getArguments().getParcelableArrayList(ListRecipesActivity.BUNDLE_STEPS);
        }
        if (ingredients == null) {
            ingredients = getArguments().getParcelableArrayList(ListRecipesActivity.BUNDLE_INGREDIENTS);
        }
        if (recipe == null) {
            recipe = getArguments().getParcelable(ListRecipesActivity.BUNDLE_RECIPE);
        }
    }

    /**
     * Verify if exists next step of recipe
     *
     * @return
     */
    private boolean hasNext() {
        return actualStep <= steps.size();
    }

    /**
     * Veirify if exists previous steps of recipe
     *
     * @return
     */
    private boolean hasPrevious() {
        return actualStep >= 1;
    }
}
