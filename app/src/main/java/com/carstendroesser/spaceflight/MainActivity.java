package com.carstendroesser.spaceflight;

import android.os.Bundle;
import android.util.DisplayMetrics;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleBaseGameActivity;

public class MainActivity extends SimpleBaseGameActivity {

    private static final String TAG = "MainActivity";

    private int mSceneWidth;
    private int mSceneHeight;

    private Camera mCamera;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mSceneWidth = displayMetrics.widthPixels;
        mSceneHeight = displayMetrics.heightPixels;

        mCamera = new Camera(0, 0, mSceneWidth, mSceneHeight);
        super.onCreate(pSavedInstanceState);
    }

    @Override
    protected void onCreateResources() {
    }

    @Override
    protected Scene onCreateScene() {
        return new Scene();
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);
    }
}