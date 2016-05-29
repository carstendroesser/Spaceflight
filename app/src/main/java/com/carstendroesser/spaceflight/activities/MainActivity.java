package com.carstendroesser.spaceflight.activities;

import android.os.Bundle;

import com.carstendroesser.spaceflight.managers.ResourceManager;
import com.carstendroesser.spaceflight.managers.SceneManager;
import com.carstendroesser.spaceflight.scenes.GamePlayingScene;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import static com.carstendroesser.spaceflight.managers.SceneManager.SceneType;

public class MainActivity extends SimpleBaseGameActivity {

    private static final String TAG = "MainActivity";

    public static int mSceneWidth = 480;
    public static int mSceneHeight = 320;

    private Camera mCamera;
    private ResourceManager mResourceManager;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        mCamera = new Camera(0, 0, mSceneWidth, mSceneHeight);
        super.onCreate(pSavedInstanceState);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        EngineOptions engineOptions = new EngineOptions(
                true,
                ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(mSceneWidth, mSceneHeight),
                mCamera);

        engineOptions.getAudioOptions().setNeedsSound(true).setNeedsMusic(true);

        return engineOptions;
    }

    @Override
    protected void onCreateResources() {
        mResourceManager = ResourceManager.getInstance();
        mResourceManager.setActivity(this);
        mResourceManager.loadResources(SceneType.GAME_PLAYING);
    }

    @Override
    protected Scene onCreateScene() {
        SceneManager.getInstance().setScene(SceneType.GAME_PLAYING);
        return new GamePlayingScene();
    }

    @Override
    protected void onPause() {
        if (mResourceManager != null && mResourceManager.mMusic != null && mResourceManager.mMusic.isPlaying()) {
            mResourceManager.mMusic.pause();
        }
        super.onPause();
    }

    @Override
    protected synchronized void onResume() {
        if (mResourceManager != null && mResourceManager.mMusic != null && !mResourceManager.mMusic.isPlaying()) {
            mResourceManager.mMusic.resume();
        }
        super.onResume();
    }
}
