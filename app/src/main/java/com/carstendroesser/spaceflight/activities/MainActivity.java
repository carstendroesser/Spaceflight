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

    // defines the resoution this game is made for
    // andEngine will scale the game later
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
        // we want the game in landscape
        // and scaled with the same ratio
        EngineOptions engineOptions = new EngineOptions(
                true,
                ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(mSceneWidth, mSceneHeight),
                mCamera);

        // tell the engine that we are about to play sound and music
        engineOptions.getAudioOptions().setNeedsSound(true).setNeedsMusic(true);

        return engineOptions;
    }

    @Override
    protected void onCreateResources() {
        // get the resourcemanager-instance
        mResourceManager = ResourceManager.getInstance();

        // set reference to this activity
        mResourceManager.setActivity(this);

        // load all resources for the GAME_PLAYING scene
        mResourceManager.loadResources(SceneType.GAME_PLAYING);
    }

    @Override
    protected Scene onCreateScene() {
        // set the current scenetype
        // so we know which scene is currently displayed
        SceneManager.getInstance().setScene(SceneType.GAME_PLAYING);

        // return an instance of this scenetype
        // which is then displayed by the engine
        return new GamePlayingScene();
    }

    @Override
    protected void onPause() {
        // when the App is brought to the background and not
        // in front anymore

        // check for music
        if (mResourceManager != null && mResourceManager.mMusic != null && mResourceManager.mMusic.isPlaying()) {
            // pause the music
            mResourceManager.mMusic.pause();
        }
        super.onPause();
    }

    @Override
    protected synchronized void onResume() {
        // when the App is brought back to the foreground

        if (mResourceManager != null && mResourceManager.mMusic != null && !mResourceManager.mMusic.isPlaying()) {
            // resume the music
            mResourceManager.mMusic.resume();
        }
        super.onResume();
    }
}
