package com.br.bestrecipes.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.br.bestrecipes.BuildConfig;
import com.br.bestrecipes.ListRecipesActivity;
import com.br.bestrecipes.R;
import com.br.bestrecipes.RecipeDetailUtil;
import com.br.bestrecipes.RecipeWidgetProvider;
import com.br.bestrecipes.bean.Ingredient;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class RecipeWidgetService extends IntentService {
    private static final String ACTION_UPDATE_TEXT_WIDGET = "com.br.bestrecipes.service.action.update_text_widget";
    private static final String ACTION_UPDATE_ALL_WIDGET = "com.br.bestrecipes.service.action.update_all_widgets";

    public static final String RECIPE_PARAM = "mParam";

    public RecipeWidgetService() {
        super("RecipeWidgetService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateTextWidget(Context context, Bundle bundle) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_UPDATE_TEXT_WIDGET);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    /**
     * Starts this service to performe update in all widgets on the screen.
     *
     * @param context
     * @param ingredients
     */
    public static void startActionUpdateAllWidgest(Context context, String ingredients) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_UPDATE_ALL_WIDGET);
        intent.putExtra(RECIPE_PARAM, ingredients);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_TEXT_WIDGET.equals(action)) {
                handleActionUpdateTextWidget(intent.getExtras());
            } else if (ACTION_UPDATE_ALL_WIDGET.equals(action)) {
//                String stringExtra = intent.getStringExtra(RECIPE_PARAM) != null ? intent.getStringExtra(RECIPE_PARAM) : getString(R.string.appwidget_text);
//                handleActionUpdateAllWidgets(stringExtra);
                handleActionUpdateAllWidgets();
            }
        }
    }

    private void handleActionUpdateAllWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_ingredients);
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds/**, ingredients*/);
    }

    /**
     * @param extras
     */
    private void handleActionUpdateTextWidget(Bundle extras) {
        String ingredients = RecipeDetailUtil.formatIngredients(extras.<Ingredient>getParcelableArrayList(ListRecipesActivity.BUNDLE_INGREDIENTS));
        saveOnPreferences(ingredients);
        startActionUpdateAllWidgest(this, ingredients);
    }

    private void saveOnPreferences(String ingredients) {
        SharedPreferences preferences = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(BuildConfig.PREF_INGREDIENT, ingredients);
        edit.commit();
    }
}
