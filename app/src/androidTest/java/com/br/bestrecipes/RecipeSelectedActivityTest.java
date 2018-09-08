package com.br.bestrecipes;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by rlima on 08/09/18.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeSelectedActivityTest {
    public static final String TAG = RecipeSelectedActivityTest.class.getSimpleName().toUpperCase();
    public static final String RECIPE_NAME = "Ingredients - Nutella Pie";

    @Rule
    public ActivityTestRule<ListRecipesActivity> mActivityTestRule = new ActivityTestRule<>(ListRecipesActivity.class);

    @Test
    public void clickItemView_OpensRecipeDetailFragment() {
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check if the select recipe is 'Nutella Pie'
        onView(withId(R.id.tv_label_ingredients)).check(matches(withText(RECIPE_NAME)));
    }
}
