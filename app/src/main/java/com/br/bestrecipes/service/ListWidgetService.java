package com.br.bestrecipes.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.br.bestrecipes.BuildConfig;
import com.br.bestrecipes.R;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    private String ingredients;

    public ListRemoteViewsFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences preferences = mContext.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        ingredients = preferences.getString(BuildConfig.PREF_INGREDIENT, null);
    }

    @Override
    public void onDestroy() {
        ingredients = null;
    }

    @Override
    public int getCount() {
        return ingredients == null ? 0 : 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (ingredients == null) {
            return null;
        }

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.appwidget_text, ingredients);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
