package com.carstendroesser.spaceflight.managers;

import com.carstendroesser.spaceflight.activities.MainActivity;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 * Created by carstendrosser on 16.05.16.
 */
public class ResourceManager {

    private static final ResourceManager instance = new ResourceManager();
    private MainActivity mActivity;

    private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
    public ITextureRegion mParallaxBackground;

    private ResourceManager() {
    }

    public static ResourceManager getInstance() {
        return instance;
    }

    public void setActivity(MainActivity pMainActivity) {
        mActivity = pMainActivity;
    }

    public MainActivity getActivity() {
        return mActivity;
    }

    public void loadResources(SceneManager.SceneType pSceneType) {
        switch (pSceneType) {
            case MENU:
                // TODO
                break;
            case GAME_PLAYING:
                loadGamePlayingSceneResources();
                break;
            case GAME_OVER:
                // TODO
                break;
        }
    }

    public void unloadResources(SceneManager.SceneType pSceneType) {
        mAutoParallaxBackgroundTexture.unload();
        switch (pSceneType) {
            case MENU:
                // TODO
                break;
            case GAME_PLAYING:
                unloadGamePlayingSceneResources();
                break;
            case GAME_OVER:
                // TODO
                break;
        }
    }

    private void unloadGamePlayingSceneResources() {
        mAutoParallaxBackgroundTexture.unload();
        mAutoParallaxBackgroundTexture = null;
        mParallaxBackground = null;
    }

    private void loadGamePlayingSceneResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 512, 1024);
        mParallaxBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture, mActivity, "background.png", 0, 150);
        mAutoParallaxBackgroundTexture.load();
    }

}