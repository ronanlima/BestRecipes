package com.br.bestrecipes.ui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.bestrecipes.ListRecipesActivity;
import com.br.bestrecipes.R;
import com.br.bestrecipes.bean.Ingredient;
import com.br.bestrecipes.bean.Recipe;
import com.br.bestrecipes.bean.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeDetailFragment extends Fragment implements ExoPlayer.EventListener {
    public static final String TAG = RecipeDetailFragment.class.getSimpleName().toUpperCase();

    private List<Step> steps;
    private List<Ingredient> ingredients;
    private Recipe recipe;
    private int actualStep = 1;

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
    @BindView(R.id.tv_error_message)
    TextView tvError;
    @BindView(R.id.progress_loading)
    ProgressBar progressBar;
    @BindView(R.id.playerView)
    SimpleExoPlayerView player;
    SimpleExoPlayer mExoPlayer;

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
        configurePlayer();
        setRetainInstance(true);
    }

    private void configurePlayer() {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            player.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
        }
    }

    /**
     * Change de video when the user moves to next/previous step.
     *
     * @param videoUrl
     */
    private void setMediaOnExoPlayer(String videoUrl) {
        Uri uri = Uri.parse(videoUrl);
        String userAgent = Util.getUserAgent(getActivity(), "BestRecipes");
        MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    /**
     * Configure the label of the actual step.
     */
    private void configureActualStep() {
        tvStepLabel.setText(String.format("Step %s - %s", actualStep, steps.get(actualStep).getShortDescription()));
        tvStepDescription.setText(steps.get(actualStep).getDescription());

        int colorPrimary = R.color.colorPrimary;
        int color_disabled = R.color.color_disabled;

        if (!hasNext()) {
            setIcTextView(tvNext, color_disabled, R.drawable.ic_next);
        } else {
            setIcTextView(tvNext, colorPrimary, R.drawable.ic_next);
        }
        if (hasPrevious()) {
            setIcTextView(tvPrev, colorPrimary, R.drawable.ic_prev);
        } else {
            setIcTextView(tvPrev, color_disabled, R.drawable.ic_prev);
        }

        if (steps.get(actualStep).getVideoURL() != null && !steps.get(actualStep).getVideoURL().isEmpty()) {
            if (isOnline()) {
                setMediaOnExoPlayer(steps.get(actualStep).getVideoURL());
                tvError.setVisibility(View.INVISIBLE);
            } else {
                tvError.setVisibility(View.VISIBLE);
                // TODO fazer alguma coisa com o player neste momento
            }
        } else {
            if (mExoPlayer != null) {
                mExoPlayer.stop();
            }
            player.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    /**
     * Use this method to change ic color for next and previous steps.
     *
     * @param tv
     * @param color
     * @param ic
     */
    private void setIcTextView(TextView tv, int color, int ic) {
        Drawable drawable = ContextCompat.getDrawable(getActivity(), ic);
        DrawableCompat.setTint(drawable, getResources().getColor(color));
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(tv, null, drawable, null, null);
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
        return actualStep < steps.size() - 1;
    }

    /**
     * Veirify if exists previous steps of recipe
     *
     * @return
     */
    private boolean hasPrevious() {
        return actualStep >= 2;
    }

    /**
     * Verify if device has connection to show de video of steps.
     *
     * @return
     */
    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            Log.d(TAG, "onPlayerStateChanged: PLAYING");
        } else if((playbackState == ExoPlayer.STATE_READY)){
            Log.d(TAG, "onPlayerStateChanged: PAUSED");
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
